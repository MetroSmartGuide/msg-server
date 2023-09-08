package skhu.msg.domain.member.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
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

    @PostMapping("/all")
    @Operation(summary = "선호도 설정", description = "회원 본인의 선호도를 설정합니다. (빠른 하차, 약냉방칸, 좌석 확보 등)")
    fun setUpPreferences(principal: Principal?, @RequestBody @Valid requestUpdatePreferences: RequestUpdatePreferences): ResponseEntity<String> {
        preferencesService.setUpPreferences(principal, requestUpdatePreferences)
        return ResponseEntity.ok("선호도 설정이 완료되었습니다.")
    }

    @PostMapping("/exit/{fastExitScore}")
    @Operation(summary = "선호도 설정 (빠른 하차)", description = "회원 본인의 빠른 하차 선호도를 설정합니다.")
    fun updateFastExitScore(principal: Principal?, @PathVariable @Valid fastExitScore: Int): ResponseEntity<String> {
        preferencesService.updateFastExitScore(principal, fastExitScore)
        return ResponseEntity.ok("빠른 하차 선호도 설정이 완료되었습니다.")
    }

    @PostMapping("/cooling/{coolingCarScore}")
    @Operation(summary = "선호도 설정 (약냉방칸)", description = "회원 본인의 약냉방칸 선호도를 설정합니다.")
    fun updateCoolingCarScore(principal: Principal?, @PathVariable @Valid coolingCarScore: Int): ResponseEntity<String> {
        preferencesService.updateCoolingCarScore(principal, coolingCarScore)
        return ResponseEntity.ok("약냉방칸 선호도 설정이 완료되었습니다.")
    }

    @PostMapping("/seat/{gettingSeatScore}")
    @Operation(summary = "선호도 설정 (좌석 확보)", description = "회원 본인의 좌석 확보 선호도를 설정합니다.")
    fun updateGettingSeatScore(principal: Principal?, @PathVariable @Valid gettingSeatScore: Int): ResponseEntity<String> {
        preferencesService.updateGettingSeatScore(principal, gettingSeatScore)
        return ResponseEntity.ok("좌석 확보 선호도 설정이 완료되었습니다.")
    }

    @GetMapping("")
    @Operation(summary = "선호도 조회", description = "회원 본인의 선호도를 조회합니다. (빠른 하차, 약냉방칸, 좌석 확보 등)")
    fun getPreferences(principal: Principal?): ResponseMemberPreferences =
        preferencesService.getPreferences(principal)

}