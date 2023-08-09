package skhu.msg.domain.auth.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class RequestLogin(
    @field:Email(message = "이메일 형식이 올바르지 않습니다.")
    @field:NotBlank(message = "이메일이 비어있습니다.")
    var email: String,

    @field:NotBlank(message = "닉네임이 비어있습니다.")
    var nickname: String,

    @field:NotBlank(message = "토큰이 비어있습니다.")
    var uid: String,
)