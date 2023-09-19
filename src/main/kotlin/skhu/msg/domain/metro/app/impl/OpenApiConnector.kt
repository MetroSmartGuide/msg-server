package skhu.msg.domain.metro.app.impl

import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import skhu.msg.domain.metro.data.SubwayStation
import skhu.msg.global.exception.ErrorCode
import skhu.msg.global.exception.GlobalException
import java.net.URL

@Component
class OpenApiConnector(
    @Value("\${api.key.seoul}") private val SEOUL_API_KEY: String,
    @Value("\${api.key.sk}") private val SK_API_KEY: String,
    @Value("\${api.key.path}") private val PATH_API_KEY: String,
) {

    @Cacheable(value = ["congestion"], key = "#subwayLine + #trainNo")
    fun getRealTimeCongestion(subwayLine: String, trainNo: String): JSONObject {
        val restTemplate = RestTemplate()

        val url = "https://apis.openapi.sk.com/puzzle/subway/congestion/rltm/trains/${subwayLine}/${trainNo}"

        val httpEntity = HttpEntity<String>(
            HttpHeaders().apply {
                set("accept", "application/json")
                set("Content-Type", "application/json")
                set("appKey", SK_API_KEY)
            })

        val response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String::class.java)

        if (!response.statusCode.is2xxSuccessful) {
            throw GlobalException(ErrorCode.NOT_FOUND_CONGESTION)
        }

        return JSONObject(response.body)
            .getJSONObject("data").getJSONObject("congestionResult") ?: throw GlobalException(ErrorCode.NOT_FOUND_CONGESTION)
    }

    fun getRealTimeArrivalTrains(stationName: String): JSONObject {
        val restTemplate = RestTemplate()

        val url = "http://swopenapi.seoul.go.kr/api/subway/$SEOUL_API_KEY/json/realtimeStationArrival/0/15/$stationName"

        val httpEntity = HttpEntity<String>(
            HttpHeaders().apply {
                set("accept", "application/json")
                set("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
            })

        val response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String::class.java)

        if (!response.statusCode.is2xxSuccessful) {
            throw GlobalException(ErrorCode.INTERNAL_SERVER_ERROR)
        }

        val responseBody = response.body ?: throw GlobalException(ErrorCode.NOT_FOUND_ARRIVAL_TRAIN)
        return JSONObject(responseBody)
    }

    fun getTransitPath(startStationCoordinate: SubwayStation, endStationCoordinate: SubwayStation): JSONObject {
        val restTemplate = RestTemplate()

        val startX = startStationCoordinate.crdnt_x
        val startY = startStationCoordinate.crdnt_y
        val endX = endStationCoordinate.crdnt_x
        val endY = endStationCoordinate.crdnt_y
        val url = "http://ws.bus.go.kr/api/rest/pathinfo/getPathInfoBySubway?serviceKey=$PATH_API_KEY&startX=$startX&startY=$startY&endX=$endX&endY=$endY&resultType=json"
        val accessUrl = URL(url).toURI()

        val httpEntity = HttpEntity<String>(
            HttpHeaders().apply {
                set("accept", "application/json")
                set("Content-Type", "application/json")
            })

        val response = restTemplate.exchange(accessUrl, HttpMethod.GET, httpEntity, String::class.java)

        if (!response.statusCode.is2xxSuccessful) {
            throw GlobalException(ErrorCode.INTERNAL_SERVER_ERROR)
        }

        val responseBody = response.body ?: throw GlobalException(ErrorCode.NOT_FOUND_TRANSIT_PATH)
        return JSONObject(responseBody)
    }

}