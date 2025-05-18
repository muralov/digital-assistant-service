package com.home.assistant.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.home.assistant.repository.AssistantRepository;
import com.home.assistant.repository.InMemoryRepository;
import com.home.assistant.repository.RedisRepository;

@Configuration
public class RepositoryConfig {

    private final static Logger logger = LoggerFactory.getLogger(RepositoryConfig.class);

    @Value("${spring.data.redis.host:}")
    private String redisHost;

    @Value("${spring.data.redis.port:}")
    private String redisPort;

    @Bean
    public AssistantRepository assistantRepository() {
        if (redisHost != null && !redisHost.isEmpty() && redisPort != null && !redisPort.isEmpty()) {
            logger.info("Using Redis storage with host: {} and port: {}", redisHost, redisPort);
            return new RedisRepository(redisHost, Integer.parseInt(redisPort));
        } else {
            logger.info("Using InMemory store since Redis is not configured");
            return new InMemoryRepository();
        }
    }
}