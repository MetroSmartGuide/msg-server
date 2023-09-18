package skhu.msg.domain.metro.app.impl

import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import skhu.msg.domain.metro.data.SubwayStation
import skhu.msg.global.exception.ErrorCode
import skhu.msg.global.exception.GlobalException
import java.net.URLEncoder

@Component
class OpenApiConnector(
    @Value("\${api.key.seoul}") private val SEOUL_API_KEY: String,
    @Value("\${api.key.sk}") private val SK_API_KEY: String,
    @Value("\${api.key.path}") private val PATH_API_KEY: String,
) {

    @Cacheable(value = ["congestion"], key = "#subwayLine + #trainNo")
    fun getRealTimeCongestion(subwayLine: String, trainNo: String): JSONObject {
        val client = OkHttpClient()

        val url = "https://apis.openapi.sk.com/puzzle/subway/congestion/rltm/trains/${subwayLine}/${trainNo}"

        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .addHeader("appKey", SK_API_KEY)
            .build()

        val response = client.newCall(request).execute()
        val json = response.body?.string() ?: throw GlobalException(ErrorCode.INTERNAL_SERVER_ERROR)

        return try {
            JSONObject(json).getJSONObject("data").getJSONObject("congestionResult")
        } catch (e: Exception) {
            throw GlobalException(ErrorCode.NOT_FOUND_CONGESTION)
        }
    }

    fun getRealTimeArrivalTrains(stationName: String): JSONObject {
        val client = OkHttpClient()

        val encodedStationName = URLEncoder.encode(stationName, "UTF-8")

        val url = "http://swopenapi.seoul.go.kr/api/subway/${SEOUL_API_KEY}/json/realtimeStationArrival/0/15/${encodedStationName}"

        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        val response = client.newCall(request).execute()
        val json = response.body?.string() ?: throw GlobalException(ErrorCode.INTERNAL_SERVER_ERROR)

        return try {
            JSONObject(json)
        } catch (e: Exception) {
            throw GlobalException(ErrorCode.NOT_FOUND_ARRIVAL_TRAIN)
        }
    }

    fun getTransitPath(startStationCoordinate: SubwayStation, endStationCoordinate: SubwayStation): JSONObject {
        val client = OkHttpClient()

        val startX = startStationCoordinate.crdnt_x
        val startY = startStationCoordinate.crdnt_y
        val endX = endStationCoordinate.crdnt_x
        val endY = endStationCoordinate.crdnt_y

        val url = "http://ws.bus.go.kr/api/rest/pathinfo/getPathInfoBySubway?ServiceKey=${PATH_API_KEY}&startX=${startX}&startY=${startY}&endX=${endX}&endY=${endY}&resultType=json"

        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .build()

        val response = client.newCall(request).execute()
        val json = response.body?.string() ?: throw GlobalException(ErrorCode.INTERNAL_SERVER_ERROR)

        return try {
            JSONObject(json)
        } catch (e: Exception) {
            throw GlobalException(ErrorCode.NOT_FOUND_TRANSIT_PATH)
        }
    }

}