package com.home.assistant.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RedisRepositoryTests {

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private RedisRepository redisRepository;

    private static final String KEY_PREFIX = "digitalassistant:";

    @BeforeEach
    void setUp() {
        // Configure redisTemplate to return the mock valueOperations when opsForValue() is called
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void save_shouldCallSetOnValueOperations() {
        Assistant assistant = new Assistant("testName", "testResponse");
        String expectedKey = KEY_PREFIX + "testName";

        redisRepository.save(assistant);

        // Verify that opsForValue().set() was called on the template with the correct key and value
        verify(redisTemplate.opsForValue()).set(expectedKey, "testResponse");
        // Alternative, more direct verification on the mocked valueOperations:
        verify(valueOperations).set(expectedKey, "testResponse");
    }

    @Test
    void get_shouldRetrieveCorrectAssistant() {
        String name = "testName";
        String response = "testResponse";
        String key = KEY_PREFIX + name;

        // Mock the behavior of valueOperations.get()
        when(valueOperations.get(key)).thenReturn(response);

        Optional<Assistant> retrieved = redisRepository.get(name);

        assertTrue(retrieved.isPresent(), "Assistant should be present");
        assertEquals(name, retrieved.get().getName(), "Assistant name should match");
        assertEquals(response, retrieved.get().getResponse(), "Assistant response should match");

        // Verify that opsForValue().get() was called with the correct key
        verify(valueOperations).get(key);
    }

    @Test
    void get_forNonExistentAssistant_shouldReturnEmptyOptional() {
        String name = "nonExistentName";
        String key = KEY_PREFIX + name;

        // Mock the behavior of valueOperations.get() to return null for a non-existent key
        when(valueOperations.get(key)).thenReturn(null);

        Optional<Assistant> retrieved = redisRepository.get(name);

        assertFalse(retrieved.isPresent(), "Optional should be empty for a non-existent assistant");

        // Verify that opsForValue().get() was called
        verify(valueOperations).get(key);
    }
}
