package com.home.assistant.repository;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.home.assistant.exception.AssistantExistsException;
import com.home.assistant.model.Assistant;

@Repository("redisRepository")
public class RedisRepository implements AssistantRepository {

    private static final String STRING_KEY_PREFIX = "digitalassistant:";
    private static final Logger logger = LoggerFactory.getLogger(RedisRepository.class);
    private final RedisTemplate<String, String> template;

    public RedisRepository(
        @Value("${redis.host:localhost}") String redisHost,
        @Value("${redis.port:6379}") int redisPort) {
        // Set your Redis host and port here
        RedisConnectionFactory connectionFactory = new LettuceConnectionFactory(redisHost, redisPort);
        ((LettuceConnectionFactory) connectionFactory).afterPropertiesSet();

        this.template = new RedisTemplate<>();
        this.template.setConnectionFactory(connectionFactory);
        this.template.afterPropertiesSet();
    }

    @Override
    public Assistant create(Assistant assistant) {
        if (this.template.opsForValue().setIfAbsent(STRING_KEY_PREFIX + assistant.getName(), assistant.getResponse())) {
            logger.info("Digital assistant created in redis: {}", assistant.getName());
        } else {
            logger.error("Assistant '" + assistant.getName() + "' already exists");
            throw new AssistantExistsException("Assistant '" + assistant.getName() + "' already exists");
        }
        
        return assistant;
    }

    @Override
    public Assistant update(Assistant assistant) {
        this.template.opsForValue().set(STRING_KEY_PREFIX + assistant.getName(), assistant.getResponse());
        logger.info("digital assistant saved in redis: {}", assistant.getName());
        return assistant;
    }
    
    @Override
    public Optional<Assistant> get(String name) {
        return Optional.ofNullable(this.template.opsForValue().get(STRING_KEY_PREFIX + name))
                .map(response -> new Assistant(name, response));
    }

}