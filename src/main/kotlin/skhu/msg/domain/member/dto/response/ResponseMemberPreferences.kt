package skhu.msg.domain.member.dto.response

data class ResponseMemberPreferences(
    var email: String,
    var nickname: String,
    var exit: Int,
    var cooling: Int,
    var seat: Int,
) {

    companion object {
        fun create(
            email: String,
            nickname: String,
            exit: Int,
            cooling: Int,
            seat: Int,
        ) = ResponseMemberPreferences(
                email = email,
                nickname = nickname,
                exit = exit,
                cooling = cooling,
                seat = seat,
            )
    }

}