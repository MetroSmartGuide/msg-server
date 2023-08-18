package skhu.msg.domain.metro.dto.response

data class ResponseTransitPath(
    val time: Int,
    val transferCount: Int,
    val pathDescription: List<PathSegment>,
) {

    companion object {
        fun create(
            time: Int,
            transferCount: Int,
            pathDescription: List<PathSegment>,
        ) = ResponseTransitPath(
            time = time,
            transferCount = transferCount,
            pathDescription = pathDescription,
        )
    }

}

// 중간 경로 정보
data class PathSegment(
    val subwayLine: String,
    val startName: String,
    val endName: String,
    val count: Int,
    val fastTransferCar: Int? = null,
) {

    companion object {
        fun create(
            subwayLine: String,
            startName: String,
            endName: String,
            count: Int,
            fastTransferCar: Int? = null,
        ) = PathSegment(
            subwayLine = subwayLine,
            startName = startName,
            endName = endName,
            count = count,
            fastTransferCar = fastTransferCar,
        )
    }

}