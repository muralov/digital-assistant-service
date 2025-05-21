package com.home.assistant.config;

import com.home.assistant.repository.AssistantRepository;
import com.home.assistant.repository.RedisRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RepositoryConfig.class})
@TestPropertySource(properties = {"spring.profiles.active=redis"})
// @ActiveProfiles("redis") // Can also be used instead of TestPropertySource for profiles
public class RepositoryConfigRedisProfileTests {

    @Autowired
    private AssistantRepository assistantRepository;

    // RedisRepository depends on RedisTemplate, so we need to provide it or mock it.
    @MockBean
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void whenRedisProfile_thenRedisRepositoryShouldBeInjected(ApplicationContext context) {
        assertNotNull(assistantRepository, "AssistantRepository should be injected");
        assertInstanceOf(RedisRepository.class, assistantRepository,
                "For 'redis' profile, AssistantRepository should be an instance of RedisRepository");
        
        // Verify that the redisTemplate mock is indeed used
        assertNotNull(context.getBean(RedisTemplate.class));
    }
}
