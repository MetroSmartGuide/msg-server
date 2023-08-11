package skhu.msg.domain.member.dto.request

import jakarta.validation.constraints.NotBlank

data class RequestUpdatePreferences(
    @field:NotBlank(message = "빠른 출구/환승 선호도가 비어있습니다.")
    var fastExitScore: Int,

    @field:NotBlank(message = "냉방칸 선호도가 비어있습니다.")
    var coolingCarScore: Int,

    @field:NotBlank(message = "좌석 선호도가 비어있습니다.")
    var gettingSeatScore: Int,
)