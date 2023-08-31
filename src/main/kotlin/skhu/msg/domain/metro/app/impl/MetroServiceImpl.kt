package skhu.msg.domain.metro.app.impl

import org.json.JSONObject
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import skhu.msg.domain.member.dao.PreferencesRepository
import skhu.msg.domain.metro.app.MetroService
import skhu.msg.domain.metro.data.FastTransfer
import skhu.msg.domain.metro.data.SubwayStation
import skhu.msg.domain.metro.data.WeakCoolingCar
import skhu.msg.domain.metro.dto.response.*
import skhu.msg.global.exception.ErrorCode
import skhu.msg.global.exception.GlobalException
import java.security.Principal

@Service
class MetroServiceImpl(
    private val preferencesRepository: PreferencesRepository,
    private val openApiConnector: OpenApiConnector,
): MetroService {

    override fun getArrivingTrainsForStation(stationName: String, subwayLine: String): List<ResponseArrivalTrain> {
        val jsonObject = openApiConnector.getRealTimeArrivalTrains(stationName)
        val jsonArray = jsonObject.getJSONArray("realtimeArrivalList")
        val responseArrivalTrainList = mutableListOf<ResponseArrivalTrain>()

        for (i in 0 until jsonArray.length()) {
            val train = jsonArray.getJSONObject(i)

            val btrainNo = train.getString("btrainNo")
            if (btrainNo.startsWith(subwayLine)) {
                val responseArrivalTrain = mapToResponseArrivalTrain(train)
                responseArrivalTrainList.add(responseArrivalTrain)
            }
        }

        return responseArrivalTrainList
    }

    override fun getCongestionForTrain(subwayLine: String, trainNo: String): ResponseCongestion {
        val jsonObject = openApiConnector.getRealTimeCongestion(subwayLine, trainNo)

        return mapToResponseCongestion(jsonObject)
    }

    override fun getShortestPathForStations(startStation: String, endStation: String): List<ResponseTransitPath> {
        val startStationCoordinate = getStationCoordinate(startStation)
        val endStationCoordinate = getStationCoordinate(endStation)

        validateStationCoordinate(startStationCoordinate, endStationCoordinate)

        val jsonObject = openApiConnector.getTransitPath(startStationCoordinate, endStationCoordinate)

        return mapToResponseTransitPathAndAddFastTransferCar(jsonObject)
    }

    @Transactional
    override fun recommendCarBasedOnMemberPreferences(principal: Principal?, subwayLine: String, trainNo: String): ResponseRecommendCar {
        principal ?: throw GlobalException(ErrorCode.INVALID_JWT)
        val memberEmail = principal.name
        val memberPreferences = getPreferencesByEmail(memberEmail)

        val memberCoolingScore = (memberPreferences.coolingCarScore ?: 1.0).toDouble()
        val memberSeatScore = (memberPreferences.gettingSeatScore ?: 1.0).toDouble()

        // 혼잡도
        val congestionJson = getCongestionForTrain(subwayLine, trainNo)
        val congestionType: Int? = congestionJson.congestionType
        val congestionCar: String? = congestionJson.congestionCar

        val carCongestionList = getCarCongestionToArray(congestionCar)

        // 가중치
        val recommendWeightList: Array<Double> = Array(carCongestionList.size) { 0.0 }

        val weakCoolingCar: List<Int> = WeakCoolingCar.getWeakCoolingCar(subwayLine)

        /**
         *  TODO: 가중치 비율 결정하기(현재 임시 비율) / 2023-08-12
         */

        for (i in carCongestionList.indices) {
            val carCongestion = carCongestionList[i]
            when {
                carCongestion in 0.0..40.0 -> recommendWeightList[i] += 3.0 * memberSeatScore
                carCongestion in 41.0..60.0 -> recommendWeightList[i] += 2.5 * memberSeatScore
                carCongestion in 61.0..80.0 -> recommendWeightList[i] += 2.0 * memberSeatScore
                carCongestion in 81.0..100.0 -> recommendWeightList[i] += 1.5 * memberSeatScore
                carCongestion >= 101.0 -> recommendWeightList[i] += 1.0 * memberSeatScore
            }

            for (j in weakCoolingCar.indices) {
                if (i == weakCoolingCar[j] - 1) {
                    recommendWeightList[i] += 3.0 * memberCoolingScore
                } else {
                    recommendWeightList[i] += 1.0 * memberCoolingScore
                }
            }
        }

        // 가중치가 가장 높은 인덱스 + 1 = 추천칸
        val recommendCar = recommendWeightList.indices.maxByOrNull { recommendWeightList[it] }?.plus(1) ?: 1

        validateRecommendCarInput(congestionType, congestionCar, weakCoolingCar, recommendCar)

        return mapToResponseRecommendCar(congestionType!!, congestionCar!!, weakCoolingCar, recommendCar)
    }

    /**
     *  getArrivingTrainsForStation()에서 사용하는 메소드
     */
    private fun mapToResponseArrivalTrain(train: JSONObject): ResponseArrivalTrain {
        return ResponseArrivalTrain.create(
            subwayId = train.getString("subwayId"),
            updnLine = train.getString("updnLine"),
            trainLineNm = train.getString("trainLineNm"),
            statnFid = train.getString("statnFid"),
            statnTid = train.getString("statnTid"),
            statnId = train.getString("statnId"),
            statnNm = train.getString("statnNm"),
            btrainNo = train.getString("btrainNo"),
            bstatnId = train.getString("bstatnId"),
            bstatnNm = train.getString("bstatnNm"),
            recptnDt = train.getString("recptnDt"),
            arvlMsg2 = train.getString("arvlMsg2"),
            arvlMsg3 = train.getString("arvlMsg3")
        )
    }

    /**
     *  getCongestionForTrain()에서 사용하는 메소드
     */
    private fun mapToResponseCongestion(congestion: JSONObject) =
        ResponseCongestion.create(
            congestionTrain = congestion.getString("congestionTrain"),
            congestionCar = congestion.getString("congestionCar"),
            congestionType = congestion.getInt("congestionType")
        )

    /**
     *  getShortestPathForStations()에서 사용하는 메소드
     */
    private fun getStationCoordinate(stationName: String) =
        SubwayStation().getInfo(stationName)
            ?: throw GlobalException(ErrorCode.INTERNAL_SERVER_ERROR)

    private fun validateStationCoordinate(startStationCoordinate: SubwayStation, endStationCoordinate: SubwayStation) {
        startStationCoordinate.crdnt_x ?: throw Exception("출발역의 좌표를 찾을 수 없습니다.")
        startStationCoordinate.crdnt_y ?: throw Exception("출발역의 좌표를 찾을 수 없습니다.")
        endStationCoordinate.crdnt_x ?: throw Exception("도착역의 좌표를 찾을 수 없습니다.")
        endStationCoordinate.crdnt_y ?: throw Exception("도착역의 좌표를 찾을 수 없습니다.")
    }

    private fun mapToResponseTransitPathAndAddFastTransferCar(jsonObject: JSONObject): List<ResponseTransitPath> {
        val itemList = jsonObject.getJSONObject("msgBody").getJSONArray("itemList")

        return itemList.map { item ->
            val pathObj = item as JSONObject
            val pathTime = pathObj.getString("time").toInt()
            val pathList = pathObj.getJSONArray("pathList")

            val pathSegmentList = pathList.mapIndexed { i, pathItem ->
                val pathItemObj = pathItem as JSONObject
                val startName = pathItemObj.getString("fname")
                val endName = pathItemObj.getString("tname")
                val pathCount = pathItemObj.getJSONArray("railLinkList").length()
                val subwayLine = pathItemObj.getString("routeNm")

                val fastTransferCar = try {
                    val nextPathItem = pathList.getJSONObject(i + 1)
                    val nextPathSubwayLine = nextPathItem.getString("routeNm")

                    getFastTransferCar(endName, subwayLine, nextPathSubwayLine)
                } catch (e: Exception) {
                    null
                }

                PathSegment.create(subwayLine, startName, endName, pathCount, fastTransferCar)
            }.toMutableList()

            val transferCount = pathSegmentList.size - 1
            ResponseTransitPath.create(pathTime, transferCount, pathSegmentList)
        }
    }

    private fun getFastTransferCar(stationName: String, beforeSubwayLine: String, afterSubwayLine: String) =
        FastTransfer.getFastTransferExitCar(stationName, beforeSubwayLine, afterSubwayLine)

    /**
     *  recommendCarBasedOnMemberPreferences()에서 사용하는 메소드
     */
    private fun getPreferencesByEmail(email: String) =
        preferencesRepository.findByMemberEmail(email)
            ?: throw GlobalException(ErrorCode.NOT_FOUND_PREFERENCES)

    private fun getCarCongestionToArray(carCongestion: String?): Array<Double> {
        carCongestion ?: throw GlobalException(ErrorCode.NOT_FOUND_CONGESTION)

        val carCongestionArray = carCongestion.split("|").toTypedArray()
        return carCongestionArray.map { it.toDouble() }.toTypedArray()
    }

    private fun validateRecommendCarInput(congestionType: Int?, congestionCar: String?, weekCoolingCar: List<Int>?, recommendCar: Int?) {
        checkNotNull(congestionType) { throw GlobalException(ErrorCode.INVALID_CONGESTION_TYPE) }
        checkNotNull(congestionCar) { throw GlobalException(ErrorCode.INVALID_CONGESTION_CAR) }
        checkNotNull(weekCoolingCar) { throw GlobalException(ErrorCode.INVALID_WEAK_COOLING_CAR) }
        checkNotNull(recommendCar) { throw GlobalException(ErrorCode.INVALID_RECOMMEND_CAR) }
    }

    private fun mapToResponseRecommendCar(congestionType: Int, congestionCar: String, weekCoolingCar: List<Int>, recommendCar: Int) =
        ResponseRecommendCar.create(
            congestionType = congestionType,
            congestionCar = congestionCar,
            weekCoolingCar = weekCoolingCar.joinToString("|"),
            recommendCar = recommendCar
        )

}