package skhu.msg.domain.member.dto.request

import jakarta.validation.constraints.NotNull

data class RequestUpdatePreferences(
    @field:NotNull(message = "빠른 출구/환승 선호도가 비어있습니다.")
    var fastExitScore: Int? = null,

    @field:NotNull(message = "냉방칸 선호도가 비어있습니다.")
    var coolingCarScore: Int? = null,

    @field:NotNull(message = "좌석 선호도가 비어있습니다.")
    var gettingSeatScore: Int? = null,
)