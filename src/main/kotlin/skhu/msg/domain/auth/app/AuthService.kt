package skhu.msg.domain.auth.app

import jakarta.servlet.http.HttpServletRequest
import skhu.msg.domain.auth.dto.request.RequestLogin
import skhu.msg.domain.auth.dto.request.RequestRefresh
import skhu.msg.domain.auth.dto.response.ResponseToken

interface AuthService {

    fun joinOrLogin(requestLogin: RequestLogin): ResponseToken

    fun refreshAccessToken(request: HttpServletRequest, requestRefresh: RequestRefresh): ResponseToken

    fun logout(request: HttpServletRequest, requestRefresh: RequestRefresh)

}