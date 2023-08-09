package skhu.msg.domain.member.dto.response

data class ResponseMember(
    var id: String,
    var email: String,
    var nickname: String,
    var uid: String,
) {

    companion object {
        fun create(
            id: String,
            email: String,
            nickname: String,
            uid: String,
        ) = ResponseMember(
            id = id,
            email = email,
            nickname = nickname,
            uid = uid,
        )
    }

}