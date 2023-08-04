package skhu.msg.domain.member.dao

import org.springframework.data.mongodb.repository.MongoRepository
import skhu.msg.domain.member.entity.Preferences

interface PreferencesRepository: MongoRepository<Preferences, String> {

    fun findByMemberEmail(memberEmail: String): Preferences?

}