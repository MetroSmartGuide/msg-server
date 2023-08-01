package skhu.msg.domain.auth.api

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import skhu.msg.domain.auth.app.AuthService
import skhu.msg.domain.auth.dto.request.RequestLogin
import skhu.msg.domain.auth.dto.response.ResponseToken

@Tag(name = "인증")
@RequestMapping("/api/v1/auth")
@RestController
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/login")
    fun joinOrLogin(@RequestBody requestLogin: RequestLogin): ResponseToken {
        return authService.joinOrLogin(requestLogin)
    }

}