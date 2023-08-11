package skhu.msg.domain.metro.dto.response

data class ResponseCongestion(
    var congestionTrain: String? = null,
    var congestionCar: String? = null,
    var congestionType: Int? = null,
) {

    companion object {
        fun create(
            congestionTrain: String? = null,
            congestionCar: String? = null,
            congestionType: Int? = null,
        ) = ResponseCongestion(
            congestionTrain = congestionTrain,
            congestionCar = congestionCar,
            congestionType = congestionType,
        )
    }

}