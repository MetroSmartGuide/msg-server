package skhu.msg.domain.member.api

import io.swagger.v3.oas.annotations.Operation
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
    @Operation(summary = "선호도 설정", description = "회원 본인의 선호도를 설정합니다. (약냉방칸, 혼잡도, 환승시간 등)")
    fun setUpPreferences(principal: Principal?, @RequestBody @Valid requestUpdatePreferences: RequestUpdatePreferences): ResponseEntity<String> {
        preferencesService.setUpPreferences(principal, requestUpdatePreferences)
        return ResponseEntity.ok("선호도 설정이 완료되었습니다.")
    }

    @GetMapping("/get")
    @Operation(summary = "선호도 조회", description = "회원 본인의 선호도를 조회합니다. (약냉방칸, 혼잡도, 환승시간 등)")
    fun getPreferences(principal: Principal?): ResponseMemberPreferences =
        preferencesService.getPreferences(principal)

}