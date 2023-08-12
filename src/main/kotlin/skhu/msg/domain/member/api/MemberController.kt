package skhu.msg.domain.member.api

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
    fun getMyInfo(principal: Principal?): ResponseMember {
        return memberService.getMyInfo(principal)
    }

    @GetMapping("/members")
    fun getUsers(): List<ResponseMember> {
        return memberService.getMembers()
    }

}