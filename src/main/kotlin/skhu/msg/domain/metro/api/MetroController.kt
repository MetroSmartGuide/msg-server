package skhu.msg.domain.metro.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
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

@Tag(name = "지하철")
@RequestMapping("/api/v1/metro")
@RestController
class MetroController(
    private val metroService: MetroService
) {

    @GetMapping("/arrival/{stationName}/{subwayLine}")
    @Operation(summary = "도착 예정 열차 조회", description = "지하철 역명과 호선을 입력하여 지하철 역에 도착 예정인 열차를 조회합니다.")
    fun getArrivingTrains(@PathVariable stationName: String, @PathVariable subwayLine: String): List<ResponseArrivalTrain> =
        metroService.getArrivingTrainsForStation(stationName, subwayLine)

    @GetMapping("/congestion/{subwayLine}/{trainNo}")
    @Operation(summary = "열차 칸별 혼잡도 조회", description = "지하철 호선명과 열차 번호를 입력하여 열차의 칸별 혼잡도를 조회합니다.")
    fun getCongestion(@PathVariable subwayLine: String, @PathVariable trainNo: String): ResponseCongestion =
        metroService.getCongestionForTrain(subwayLine, trainNo)

    @GetMapping("/path/{startStation}/{endStation}")
    @Operation(summary = "최단 경로 조회", description = "출발역과 도착역 사이의 최단 경로를 조회합니다.")
    fun getShortestPath(@PathVariable startStation: String, @PathVariable endStation: String): List<ResponseTransitPath> =
        metroService.getShortestPathForStations(startStation, endStation)

    @GetMapping("/recommend/{subwayLine}/{trainNo}")
    @Operation(summary = "열차 탑승칸 추천", description = "지하철 호선과 열차의 번호를 입력하여 열차의 상태와 회원의 선호도를 기반으로 탑승할 열차 칸을 추천합니다.")
    fun getRecommendation(principal: Principal?, @PathVariable subwayLine: String, @PathVariable trainNo: String): ResponseRecommendCar =
        metroService.recommendCarBasedOnMemberPreferences(principal, subwayLine, trainNo)

}