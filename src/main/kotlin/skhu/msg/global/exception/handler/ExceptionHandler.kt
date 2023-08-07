package skhu.msg.global.exception.handler

import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import skhu.msg.global.exception.GlobalException
import skhu.msg.global.exception.response.ResponseException

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(GlobalException::class)
    fun handleGlobalException(e: GlobalException): ResponseException {
        return ResponseException.create(
            statusCode = e.error.status.value(),
            error = e.error,
            message = e.error.message,
        )
    }

}