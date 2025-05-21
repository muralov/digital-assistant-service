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
@TestPropertySource(properties = {"spring.profiles.active=default"})
public class RepositoryConfigDefaultProfileTests {

    @Autowired
    private AssistantRepository assistantRepository;

    @MockBean
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void whenDefaultProfile_thenInMemoryRepositoryShouldBeInjected() {
        assertNotNull(assistantRepository, "AssistantRepository should be injected");
        assertInstanceOf(InMemoryRepository.class, assistantRepository,
                "For 'default' profile, AssistantRepository should be an instance of InMemoryRepository");
    }
}
