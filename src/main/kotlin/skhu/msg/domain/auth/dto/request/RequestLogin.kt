package skhu.msg.domain.auth.dto.request

data class RequestLogin(
    var email: String,
    var nickname: String,
    var uid: String,
)