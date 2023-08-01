package skhu.msg.domain.auth.app

import skhu.msg.domain.auth.dto.request.RequestLogin
import skhu.msg.domain.auth.dto.response.ResponseToken

interface AuthService {

    fun joinOrLogin(requestLogin: RequestLogin): ResponseToken

}