package skhu.msg.domain.metro.dto.response

data class ResponseRecommendCar(
    val congestionType: Int? = null,
    val congestionCar: String? = null,
    val weekCoolingCar: String? = null,
    val recommendCar: Int? = null,
) {

    companion object {
        fun create(
            congestionType: Int? = null,
            congestionCar: String? = null,
            weekCoolingCar: String? = null,
            recommendCar: Int? = null,
        ) = ResponseRecommendCar(
            congestionType = congestionType,
            congestionCar = congestionCar,
            weekCoolingCar = weekCoolingCar,
            recommendCar = recommendCar,
        )
    }

}