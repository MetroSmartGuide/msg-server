package skhu.msg.domain.auth.dto.response

data class ResponseToken(
    var accessToken: String,
) {

    companion object {
        fun create(accessToken: String) = ResponseToken(accessToken)
    }

}