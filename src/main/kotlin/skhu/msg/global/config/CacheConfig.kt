package skhu.msg.global.config

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@EnableCaching
@Configuration
class CacheConfig {

    final val cacheNames = "congestion"
    final val expireAfterWrite = 60L
    final val maximumSize = 1000L
    final val caffeine = Caffeine.newBuilder()
        .expireAfterWrite(expireAfterWrite, TimeUnit.SECONDS)
        .maximumSize(maximumSize)
        .build<Any, Any>()

    @Bean
    fun cacheManager(): CacheManager {
        val cacheManager = SimpleCacheManager()
        val caches = mutableListOf(CaffeineCache(cacheNames, caffeine))
        cacheManager.setCaches(caches)
        return cacheManager
    }

}