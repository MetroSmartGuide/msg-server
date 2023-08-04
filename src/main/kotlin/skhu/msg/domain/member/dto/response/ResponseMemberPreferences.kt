package skhu.msg.domain.member.dto.response

data class ResponseMemberPreferences(
    var email: String? = null,
    var nickname: String? = null,
    var exit: Int? = null,
    var cooling: Int? = null,
    var seat: Int? = null,
) {

    companion object {
        fun create(
            email: String? = null,
            nickname: String? = null,
            exit: Int? = null,
            cooling: Int? = null,
            seat: Int? = null,
        ): ResponseMemberPreferences {
            if (email == null || nickname == null || exit == null || cooling == null || seat == null) {
                throw IllegalArgumentException("ResponseMemberPreferences.of: arguments must not be null")
            }

            return ResponseMemberPreferences(
                email = email,
                nickname = nickname,
                exit = exit,
                cooling = cooling,
                seat = seat,
            )
        }
    }

}