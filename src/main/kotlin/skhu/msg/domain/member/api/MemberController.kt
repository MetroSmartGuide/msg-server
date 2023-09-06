package skhu.msg.domain.member.api

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import skhu.msg.domain.member.app.MemberService
import skhu.msg.domain.member.dto.response.ResponseMember
import java.security.Principal

@Tag(name = "회원")
@RequestMapping("/api/v1/member")
@RestController
class MemberController(
    private val memberService: MemberService,
) {

    @GetMapping("/info")
    @Operation(summary = "내 정보 조회", description = "로그인 된 본인의 정보를 조회합니다.")
    fun getMyInfo(principal: Principal?): ResponseMember =
        memberService.getMyInfo(principal)

    @GetMapping("/members")
    @Operation(summary = "회원 목록 조회", description = "모든 회원의 정보를 조회합니다. (테스트용)")
    fun getUsers(): List<ResponseMember> =
        memberService.getMembers()

}