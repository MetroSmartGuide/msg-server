package skhu.msg.domain.member.api

import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import skhu.msg.domain.member.app.PreferencesService
import skhu.msg.domain.member.dto.request.RequestUpdatePreferences
import skhu.msg.domain.member.dto.response.ResponseMemberPreferences
import java.security.Principal

@Tag(name = "회원-설정")
@RequestMapping("/api/v1/preferences")
@RestController
class PreferencesController(
    private val preferencesService: PreferencesService,
) {

    @PostMapping("/set")
    fun setUpPreferences(principal: Principal?, @RequestBody @Valid requestUpdatePreferences: RequestUpdatePreferences): ResponseEntity<String> {
        preferencesService.setUpPreferences(principal, requestUpdatePreferences)
        return ResponseEntity.ok("선호도 설정이 완료되었습니다.")
    }

    @GetMapping("/get")
    fun getPreferences(principal: Principal?): ResponseMemberPreferences =
        preferencesService.getPreferences(principal)

}