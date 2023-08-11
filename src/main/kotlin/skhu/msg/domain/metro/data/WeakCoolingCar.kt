package skhu.msg.domain.metro.data

import skhu.msg.global.exception.ErrorCode
import skhu.msg.global.exception.GlobalException

class WeakCoolingCar {

    companion object {
        private val weakCoolingCar: Map<String, List<Int>> = mapOf(
            "1호선" to listOf(4, 7),
            "2호선" to listOf(),
            "3호선" to listOf(4, 7),
            "4호선" to listOf(4, 7),
            "5호선" to listOf(4, 5),
            "6호선" to listOf(4, 5),
            "7호선" to listOf(4, 7),
            "8호선" to listOf(3, 4),
            "9호선" to listOf(),
            "경의중앙선" to listOf(4, 5),
            "수인분당선" to listOf(3, 4),
            "신분당선" to listOf(),
            "공항철도" to listOf(3, 4),
        )

        fun getWeakCoolingCar(line: String): List<Int> =
            listOf(
                "1호선", "2호선", "3호선", "4호선", "5호선",
                "6호선", "7호선", "8호선", "9호선", "경의중앙선",
                "수인분당선", "신분당선", "공항철도"
            ).firstOrNull { it.startsWith(line) }?.let { weakCoolingCar[it] }
                ?: throw GlobalException(ErrorCode.NOT_FOUND_SUBWAY_LINE)
    }

}