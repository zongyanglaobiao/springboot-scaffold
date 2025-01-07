package aks.com.web.config;

import aks.com.web.toolkit.redis.RedisUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis序列化
 * @author xxl
 * @since 2023/9/16
 */
@Configuration
public class RedisConfigure {

    /**
     * 序列化相关的配置
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> redisTemplate  = new RedisTemplate<>();
        //支持事务
        redisTemplate.setEnableTransactionSupport(true);
        //json方式序列化
        Jackson2JsonRedisSerializer<Object> jackson = new Jackson2JsonRedisSerializer<>(Object.class);
        redisTemplate.setConnectionFactory(factory);
        //String方式序列化
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        //设置所有的key为string方式序列化
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        //设置所有的value为jackson方式序列化
        redisTemplate.setValueSerializer(jackson);
        redisTemplate.setHashValueSerializer(jackson);
        redisTemplate.afterPropertiesSet();

        //redis工具类
        RedisUtils.setRedisTemplate(redisTemplate);
        return redisTemplate;
    }
}
