package skhu.msg.domain.member.dto.request

class RequestUpdatePreferences(
    var email: String,
    var exit: Int? = null,
    var cooling: Int? = null,
    var seat: Int? = null,
)