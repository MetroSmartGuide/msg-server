package skhu.msg.domain.auth.dto.request

import jakarta.validation.constraints.NotBlank

data class RequestRefresh(
    @field:NotBlank(message = "refreshToken이 비어있습니다.")
    var refreshToken: String? = null,
)