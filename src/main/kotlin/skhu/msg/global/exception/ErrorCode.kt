package skhu.msg.global.exception

import org.springframework.http.HttpStatus

open class GlobalException(val error: ErrorCode): RuntimeException()

enum class ErrorCode(
    val status: HttpStatus,
    val message: String,
) {

    // 400 - Bad Request
    INVALID_COORDINATE_INPUT(HttpStatus.BAD_REQUEST, "좌표 입력이 올바르지 않습니다"),
    INVALID_SUBWAY_LINE_INPUT(HttpStatus.BAD_REQUEST, "지하철 노선 입력이 올바르지 않습니다"),
    INVALID_TRAIN_NO_INPUT(HttpStatus.BAD_REQUEST, "지하철 차량 번호 입력이 올바르지 않습니다"),
    INVALID_STATION_NAME_INPUT(HttpStatus.BAD_REQUEST, "지하철역 이름 입력이 올바르지 않습니다"),

    // 404 - Not Found
    NOT_FOUND_STATION(HttpStatus.NOT_FOUND, "지하철역을 찾을 수 없습니다"),
    NOT_FOUND_CONGESTION(HttpStatus.NOT_FOUND, "혼잡도 정보를 찾을 수 없습니다"),
    NOT_FOUND_ARRIVAL_TRAIN(HttpStatus.NOT_FOUND, "도착 예정인 열차를 찾을 수 없습니다"),
    NOT_FOUND_SHORTEST_PATH(HttpStatus.NOT_FOUND, "최단 경로를 찾을 수 없습니다"),

    // 500 - Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러가 발생했습니다"),

}