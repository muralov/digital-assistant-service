package com.home.assistant.config;

import com.home.assistant.repository.AssistantRepository;
import com.home.assistant.repository.InMemoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {RepositoryConfig.class})
@TestPropertySource(properties = {"spring.profiles.active=in-memory"})
public class RepositoryConfigInMemoryProfileTests {

    @Autowired
    private AssistantRepository assistantRepository;

    // Mock RedisTemplate because it might be on the classpath, 
    // but we don't want any Redis interaction for in-memory tests.
    // RepositoryConfig doesn't directly use it when 'in-memory' is active,
    // but it's good practice if other auto-configurations might pick it up.
    @MockBean
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void whenInMemoryProfile_thenInMemoryRepositoryShouldBeInjected() {
        assertNotNull(assistantRepository, "AssistantRepository should be injected");
        assertInstanceOf(InMemoryRepository.class, assistantRepository,
                "For 'in-memory' profile, AssistantRepository should be an instance of InMemoryRepository");
    }
}
