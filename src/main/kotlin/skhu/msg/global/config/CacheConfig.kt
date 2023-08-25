package skhu.msg.global.config

import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.concurrent.ConcurrentMapCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@EnableCaching
@Configuration
class CacheConfig {

    @Bean
    fun cacheManager() =
        ConcurrentMapCacheManager("metroCache")

}