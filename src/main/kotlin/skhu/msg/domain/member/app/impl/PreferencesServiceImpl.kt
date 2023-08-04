package skhu.msg.domain.member.app.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import skhu.msg.domain.member.app.PreferencesService
import skhu.msg.domain.member.dao.MemberRepository
import skhu.msg.domain.member.dao.PreferencesRepository
import skhu.msg.domain.member.dto.request.RequestUpdatePreferences
import skhu.msg.domain.member.dto.response.ResponseMemberPreferences
import skhu.msg.domain.member.entity.Preferences
import java.security.Principal

@Service
class PreferencesServiceImpl(
    private val preferencesRepository: PreferencesRepository,
    private val memberRepository: MemberRepository,
): PreferencesService {

    @Transactional
    override fun setUpPreferences(requestUpdatePreferences: RequestUpdatePreferences) {
        val memberEmail = requestUpdatePreferences.email
        val memberExit = requestUpdatePreferences.exit
        val memberCooling = requestUpdatePreferences.cooling
        val memberSeat = requestUpdatePreferences.seat

        val preferences = preferencesRepository.findByMemberEmail(memberEmail)
            ?: run {
                val newPreferences = Preferences.create(
                    memberEmail = memberEmail,
                    exitScore = memberExit,
                    coolingScore = memberCooling,
                    seatScore = memberSeat
                )
                preferencesRepository.save(newPreferences)
            }

        preferences.updateExitScore(memberExit)
        preferences.updateCoolingScore(memberCooling)
        preferences.updateSeatScore(memberSeat)
    }

    @Transactional(readOnly = true)
    override fun getPreferences(principal: Principal): ResponseMemberPreferences {
        val memberEmail = principal.name
        val member = getMemberByEmail(memberEmail)
        val preferences = getPreferencesByEmail(memberEmail)

        return ResponseMemberPreferences.create(
            email = memberEmail,
            nickname = member.nickname,
            exit = preferences.exitScore,
            cooling = preferences.coolingScore,
            seat = preferences.seatScore
        )
    }

    private fun getMemberByEmail(memberEmail: String) =
        memberRepository.findByEmail(memberEmail)
            ?: throw Exception("회원 정보를 찾을 수 없습니다.")

    private fun getPreferencesByEmail(memberEmail: String) =
        preferencesRepository.findByMemberEmail(memberEmail)
            ?: throw Exception("회원 정보를 찾을 수 없습니다.")

}