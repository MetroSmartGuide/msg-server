package skhu.msg.domain.auth.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import skhu.msg.domain.auth.app.AuthService
import skhu.msg.domain.auth.dto.request.RequestLogin
import skhu.msg.domain.auth.dto.request.RequestRefresh
import skhu.msg.domain.auth.dto.response.ResponseToken

@Tag(name = "인증")
@RequestMapping("/api/v1/auth")
@RestController
class AuthController(
    private val authService: AuthService,
) {

    @PostMapping("/login")
    @Operation(summary = "로그인 또는 회원가입", description = "회원 정보를 사용해 이미 회원인 경우 로그인, 아닌 경우 회원가입 후 로그인을 수행합니다.")
    fun joinOrLogin(@RequestBody @Valid requestLogin: RequestLogin): ResponseToken =
        authService.joinOrLogin(requestLogin)

    @PostMapping("/refresh")
    @Operation(summary = "액세스 토큰 갱신", description = "리프레시 토큰을 사용해 액세스 토큰을 갱신합니다.")
    fun refreshAccessToken(request: HttpServletRequest, @RequestBody @Valid requestRefresh: RequestRefresh): ResponseToken =
         authService.refreshAccessToken(request, requestRefresh)

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "액세스 토큰을 사용하지 못하게 만들어 로그아웃 기능을 수행합니다.")
    fun logout(request: HttpServletRequest, @RequestBody @Valid requestRefresh: RequestRefresh) =
        authService.logout(request, requestRefresh)

}