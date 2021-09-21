package pink.zak.api.riptide.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class RedisCachingConfig {

    @Bean
    public RedisCacheConfiguration generateConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .prefixCacheNameWith("riptide-")
                .entryTtl(Duration.ofMinutes(120))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    @Bean
    @Autowired
    RedisConnection generateConnection(RedisConnectionFactory connectionFactory) {
        return connectionFactory.getConnection();
    }

    @Bean
    @Autowired
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(serializer);
        template.setHashKeySerializer(serializer);

        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);

        template.setEnableTransactionSupport(true);
        template.afterPropertiesSet();

        return template;
    }
}
