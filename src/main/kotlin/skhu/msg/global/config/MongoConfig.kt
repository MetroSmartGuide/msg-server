package skhu.msg.global.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration

@Configuration
class MongoConfig: AbstractMongoClientConfiguration() {

    @Value("\${spring.data.mongodb.uri}") private lateinit var mongoUri: String
    @Value("\${spring.data.mongodb.database}") private lateinit var databaseName: String

    override fun getDatabaseName(): String {
        return databaseName
    }

    override fun mongoClient(): MongoClient {
        val connectionString = ConnectionString(mongoUri)

        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build()

        return MongoClients.create(mongoClientSettings)
    }

}