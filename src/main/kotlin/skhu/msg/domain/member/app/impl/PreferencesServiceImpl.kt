package skhu.msg.domain.member.app.impl

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import skhu.msg.domain.member.app.PreferencesService
import skhu.msg.domain.member.dao.MemberRepository
import skhu.msg.domain.member.dao.PreferencesRepository
import skhu.msg.domain.member.dto.request.RequestUpdatePreferences
import skhu.msg.domain.member.dto.response.ResponseMemberPreferences
import skhu.msg.domain.member.entity.Member
import skhu.msg.domain.member.entity.Preferences
import skhu.msg.global.exception.ErrorCode
import skhu.msg.global.exception.GlobalException
import java.security.Principal

@Service
class PreferencesServiceImpl(
    private val preferencesRepository: PreferencesRepository,
    private val memberRepository: MemberRepository,
): PreferencesService {

    @Transactional
    override fun setUpPreferences(requestUpdatePreferences: RequestUpdatePreferences) {
        val memberEmail = requestUpdatePreferences.email
        val newFastExitScore = requestUpdatePreferences.fastExitScore
        val newCoolingCarScore = requestUpdatePreferences.coolingCarScore
        val newGettingSeatScore = requestUpdatePreferences.gettingSeatScore

        val preferences = preferencesRepository.findByMemberEmail(memberEmail)
            ?: run {
                val newPreferences = Preferences.create(
                    fastExitScore = memberEmail,
                    exitScore = newFastExitScore,
                    coolingCarScore = newCoolingCarScore,
                    gettingSeatScore = newGettingSeatScore
                )
                preferencesRepository.save(newPreferences)
            }

        preferences.updateExitScore(newFastExitScore)
        preferences.updateCoolingScore(newCoolingCarScore)
        preferences.updateSeatScore(newGettingSeatScore)
    }

    @Transactional(readOnly = true)
    override fun getPreferences(principal: Principal): ResponseMemberPreferences {
        val memberEmail = principal.name
        val member = getMemberByEmail(memberEmail)
        val preferences = getPreferencesByEmail(memberEmail)

        validateMember(member)
        validatePreferences(preferences)

        return mapToResponseMemberPreferences(member, preferences)
    }

    private fun getMemberByEmail(memberEmail: String) =
        memberRepository.findByEmail(memberEmail)
            ?: throw GlobalException(ErrorCode.NOT_FOUND_MEMBER)

    private fun getPreferencesByEmail(memberEmail: String) =
        preferencesRepository.findByMemberEmail(memberEmail)
            ?: throw GlobalException(ErrorCode.NOT_FOUND_PREFERENCES)

    private fun validateMember(member: Member) {
        checkNotNull(member.id) { throw GlobalException(ErrorCode.INVALID_FILED_VALUE) }
        checkNotNull(member.nickname) { throw GlobalException(ErrorCode.INVALID_FILED_VALUE) }
        checkNotNull(member.uid) { throw GlobalException(ErrorCode.INVALID_FILED_VALUE) }
    }

    private fun validatePreferences(preferences: Preferences) {
        checkNotNull(preferences.fastExitScore) { throw GlobalException(ErrorCode.INVALID_FILED_VALUE) }
        checkNotNull(preferences.coolingCarScore) { throw GlobalException(ErrorCode.INVALID_FILED_VALUE) }
        checkNotNull(preferences.gettingSeatScore) { throw GlobalException(ErrorCode.INVALID_FILED_VALUE) }
    }

    private fun mapToResponseMemberPreferences(member: Member, preferences: Preferences) =
        ResponseMemberPreferences.create(
            email = preferences.memberEmail,
            nickname = member.nickname!!,
            exit = preferences.fastExitScore!!,
            cooling = preferences.coolingCarScore!!,
            seat = preferences.gettingSeatScore!!,
        )

}