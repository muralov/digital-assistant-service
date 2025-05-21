package com.home.assistant.repository;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository("redisRepository")
public class RedisRepository implements AssistantRepository {

    private static final String STRING_KEY_PREFIX = "digitalassistant:";
    private static final Logger logger = LoggerFactory.getLogger(RedisRepository.class);
    private final RedisTemplate<String, String> template;

    public RedisRepository(RedisTemplate<String, String> template) {
        this.template = template;
    }

    @Override
    public Assistant save(Assistant assistant) {
        this.template.opsForValue().set(STRING_KEY_PREFIX + assistant.getName(), assistant.getResponse());
        logger.info("digital assistant  '{}' saved in redis", assistant.getName());
        return assistant;
    }
    
    @Override
    public Optional<Assistant> get(String name) {
        return Optional.ofNullable(this.template.opsForValue().get(STRING_KEY_PREFIX + name))
                .map(response -> new Assistant(name, response));
    }

}