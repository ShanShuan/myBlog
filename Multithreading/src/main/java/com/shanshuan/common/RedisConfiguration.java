package com.shanshuan.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Created by wangzifeng on 2020/3/27.
 */
@Configuration
public class RedisConfiguration {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // key采用String的序列化方式
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());  // value序列化方式采用jackson
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());  // hash的key也采用String的序列化方式
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());   // hash的value序列化方式采用jackson
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

}

