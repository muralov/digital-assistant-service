package com.home.assistant.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;

import com.home.assistant.repository.AssistantRepository;
import com.home.assistant.repository.InMemoryRepository;
import com.home.assistant.repository.RedisRepository;

@Configuration
public class RepositoryConfig {

    private final static Logger logger = LoggerFactory.getLogger(RepositoryConfig.class);

    @Bean
    @Profile({"in-memory", "default"})
    public AssistantRepository inMemoryRepository() {
        logger.info("Using InMemory store");
        return new InMemoryRepository();
    }

    @Bean
    @Profile("redis")
    public AssistantRepository redisRepository(RedisTemplate<String, String> template) {
        logger.info("Using Redis storage");
        return new RedisRepository(template);
    }
}