package skhu.msg.global.exception.handler

import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import skhu.msg.global.exception.ErrorCode
import skhu.msg.global.exception.GlobalException
import skhu.msg.global.exception.response.ResponseException

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(GlobalException::class)
    fun handleGlobalException(ex: GlobalException): ResponseException {
        return ResponseException.create(
            statusCode = ex.error.status.value(),
            error = ex.error,
            message = ex.error.message,
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(ex: MethodArgumentNotValidException): ResponseException {
        val errorReason= ex.bindingResult.fieldError?.field ?: ""
        val errorMessage = ErrorCode.INVALID_REQUEST_INPUT.message
        val customErrorMessage = "$errorMessage / $errorReason"

        return ResponseException.create(
            statusCode = 400,
            error = ErrorCode.INVALID_REQUEST_INPUT,
            message = customErrorMessage,
        )
    }

}