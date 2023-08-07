package skhu.msg.global.exception.response

import skhu.msg.global.exception.ErrorCode

data class ResponseException(
    val statusCode: Int,
    val errorCode: String,
    val message: String,
) {

    companion object {
        fun create(
            statusCode: Int,
            error: ErrorCode,
            message: String,
        ): ResponseException {
            return ResponseException(
                statusCode = statusCode,
                errorCode = error.name,
                message = message,
            )
        }
    }

}