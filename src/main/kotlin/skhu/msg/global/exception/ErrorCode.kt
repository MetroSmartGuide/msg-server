package skhu.msg.global.exception

import org.springframework.http.HttpStatus

open class GlobalException(val error: ErrorCode): RuntimeException()

enum class ErrorCode(
    val status: HttpStatus,
    val message: String,
) {

    // 400 - Bad Request
    INVALID_REQUEST_INPUT(HttpStatus.BAD_REQUEST, "요청 입력이 올바르지 않습니다."),
    INVALID_FILED_VALUE(HttpStatus.BAD_REQUEST, "필드 값이 올바르지 않습니다."),
    INVALID_UPDATE_VALUE(HttpStatus.BAD_REQUEST, "수정 값이 올바르지 않습니다."),
    INVALID_COORDINATE_INPUT(HttpStatus.BAD_REQUEST, "좌표 입력이 올바르지 않습니다."),
    INVALID_SUBWAY_LINE_INPUT(HttpStatus.BAD_REQUEST, "지하철 노선 입력이 올바르지 않습니다."),
    INVALID_TRAIN_NO_INPUT(HttpStatus.BAD_REQUEST, "지하철 차량 번호 입력이 올바르지 않습니다."),
    INVALID_STATION_NAME_INPUT(HttpStatus.BAD_REQUEST, "지하철역 이름 입력이 올바르지 않습니다."),
    INVALID_LOGIN_INPUT(HttpStatus.BAD_REQUEST, "로그인 정보가 올바르지 않습니다."),
    INVALID_CONGESTION_TYPE(HttpStatus.BAD_REQUEST, "혼잡도 타입 값이 올바르지 않습니다."),
    INVALID_CONGESTION_CAR(HttpStatus.BAD_REQUEST, "혼잡도 정보가 올바르지 않습니다."),
    INVALID_WEAK_COOLING_CAR(HttpStatus.BAD_REQUEST, "약냉방 차량 정보가 올바르지 않습니다."),
    INVALID_RECOMMEND_CAR(HttpStatus.BAD_REQUEST, "추천 차량 값이 올바르지 않습니다."),
    INVALID_JWT(HttpStatus.BAD_REQUEST, "토큰 값이 비어있거나 올바르지 않습니다."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "이메일 값이 올바르지 않습니다."),

    // 403 - Forbidden
    FORBIDDEN_TOKEN(HttpStatus.FORBIDDEN, "권한 정보가 없는 토큰입니다."),

    // 404 - Not Found
    NOT_FOUND_STATION(HttpStatus.NOT_FOUND, "지하철역을 찾을 수 없습니다."),
    NOT_FOUND_CONGESTION(HttpStatus.NOT_FOUND, "혼잡도 정보를 찾을 수 없습니다."),
    NOT_FOUND_ARRIVAL_TRAIN(HttpStatus.NOT_FOUND, "도착 예정인 열차를 찾을 수 없습니다."),
    NOT_FOUND_SHORTEST_PATH(HttpStatus.NOT_FOUND, "최단 경로를 찾을 수 없습니다."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    NOT_FOUND_PREFERENCES(HttpStatus.NOT_FOUND, "회원의 선호도 정보를 찾을 수 없습니다."),
    NOT_FOUND_SUBWAY_LINE(HttpStatus.NOT_FOUND, "지하철 노선을 찾을 수 없습니다."),
    NOT_FOUND_FAST_TRANSFER_EXIT(HttpStatus.NOT_FOUND, "빠른 환승 출구를 찾을 수 없습니다."),

    // 500 - Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러가 발생했습니다."),
    INTERNAL_SERVER_ERROR_REDIS(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러가 발생했습니다. (Redis)"),
    INTERNAL_SERVER_ERROR_JWT(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러가 발생했습니다. (JWT)"),
    INTERNAL_SERVER_ERROR_LOGIN(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러가 발생했습니다. (로그인)"),

}