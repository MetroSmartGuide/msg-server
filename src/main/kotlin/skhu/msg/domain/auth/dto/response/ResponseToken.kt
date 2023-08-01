package skhu.msg.domain.auth.dto.response

data class ResponseToken(
    var accessToken: String? = null,
) {

    companion object {
        fun create(
            accessToken: String? = null,
        ): ResponseToken {
            if (accessToken == null) {
                throw IllegalArgumentException("ResponseToken.of: arguments must not be null")
            }

            return ResponseToken(
                accessToken = accessToken,
            )
        }
    }

}