package skhu.msg.domain.auth.dto.response

data class ResponseToken(
    var accessToken: String,
    var refreshToken: String,
) {

    companion object {
        fun create(
            accessToken: String,
            refreshToken: String,
        ) = ResponseToken(
            accessToken = accessToken,
            refreshToken = refreshToken,
        )
    }

}