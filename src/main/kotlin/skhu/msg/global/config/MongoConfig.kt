package skhu.msg.global.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration

@Configuration
class MongoConfig: AbstractMongoClientConfiguration() {

    @Value("\${spring.data.mongodb.uri}") private lateinit var mongoUri: String
    @Value("\${spring.data.mongodb.database}") private lateinit var databaseName: String
    @Value("\${spring.data.mongodb.username}") private lateinit var username: String
    @Value("\${spring.data.mongodb.password}") private lateinit var password: String

    override fun getDatabaseName(): String {
        return databaseName
    }

    override fun mongoClient(): MongoClient {
        val connectionString = ConnectionString(mongoUri)

        val credentials = MongoCredential.createCredential(username, databaseName, password.toCharArray())

        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .credential(credentials)
            .build()

        return MongoClients.create(mongoClientSettings)
    }

}