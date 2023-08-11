package skhu.msg.domain.metro.app

import skhu.msg.domain.metro.dto.response.ResponseArrivalTrain
import skhu.msg.domain.metro.dto.response.ResponseCongestion
import skhu.msg.domain.metro.dto.response.ResponseTransitPath
import skhu.msg.domain.metro.dto.response.ResponseRecommendCar
import java.security.Principal

interface MetroService {

    fun getArrivingTrainsForStation(stationName: String, subwayLine: String): List<ResponseArrivalTrain>

    fun getCongestionForTrain(subwayLine: String, trainNo: String): ResponseCongestion

    fun getShortestPathForStations(startStation: String, endStation: String): List<ResponseTransitPath>

    fun recommendCarBasedOnMemberPreferences(principal: Principal, subwayLine: String, trainNo: String): ResponseRecommendCar

}