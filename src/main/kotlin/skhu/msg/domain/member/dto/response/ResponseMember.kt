package skhu.msg.domain.member.dto.response

data class ResponseMember(
    var id: String? = null,
    var email: String? = null,
    var nickname: String? = null,
    var uid: String? = null,
) {

    companion object {
        fun create(
            id: String? = null,
            email: String? = null,
            nickname: String? = null,
            uid: String? = null,
        ): ResponseMember {
            if (id == null || email == null || nickname == null || uid == null) {
                throw IllegalArgumentException("ResponseMember.of: arguments must not be null")
            }

            return ResponseMember(
                id = id,
                email = email,
                nickname = nickname,
                uid = uid,
            )
        }
    }

}