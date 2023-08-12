package skhu.msg.domain.metro.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import skhu.msg.domain.metro.app.MetroService
import skhu.msg.domain.metro.dto.response.ResponseArrivalTrain
import skhu.msg.domain.metro.dto.response.ResponseCongestion
import skhu.msg.domain.metro.dto.response.ResponseTransitPath
import skhu.msg.domain.metro.dto.response.ResponseRecommendCar
import java.security.Principal

@RequestMapping("/api/v1/metro")
@RestController
class MetroController(
    private val metroService: MetroService
) {

    @GetMapping("/arrival/{stationName}/{subwayLine}")
    fun getArrivingTrains(@PathVariable stationName: String, @PathVariable subwayLine: String): List<ResponseArrivalTrain> {
        return metroService.getArrivingTrainsForStation(stationName, subwayLine)
    }

    @GetMapping("/congestion/{subwayLine}/{trainNo}")
    fun getCongestion(@PathVariable subwayLine: String, @PathVariable trainNo: String): ResponseCongestion {
        return metroService.getCongestionForTrain(subwayLine, trainNo)
    }

    @GetMapping("/path/{startStation}/{endStation}")
    fun getShortestPath(@PathVariable startStation: String, @PathVariable endStation: String): List<ResponseTransitPath> {
        return metroService.getShortestPathForStations(startStation, endStation)
    }

    @GetMapping("/recommend/{subwayLine}/{trainNo}")
    fun getRecommendation(principal: Principal?, @PathVariable subwayLine: String, @PathVariable trainNo: String): ResponseRecommendCar {
        return metroService.recommendCarBasedOnMemberPreferences(principal, subwayLine, trainNo)
    }

}