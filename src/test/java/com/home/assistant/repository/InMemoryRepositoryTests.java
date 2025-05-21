package com.home.assistant.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryRepositoryTests {

    private InMemoryRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryRepository();
    }

    @Test
    void save_shouldStoreAssistant() {
        Assistant assistant = new Assistant("testName", "testResponse");
        repository.save(assistant);

        Optional<Assistant> retrieved = repository.get("testName");
        assertTrue(retrieved.isPresent(), "Assistant should be present after saving");
        assertEquals("testResponse", retrieved.get().getResponse(), "Response should match");
        assertEquals("testName", retrieved.get().getName(), "Name should match");
    }

    @Test
    void get_shouldRetrieveCorrectAssistant() {
        Assistant assistant1 = new Assistant("testName1", "testResponse1");
        Assistant assistant2 = new Assistant("testName2", "testResponse2");
        repository.save(assistant1);
        repository.save(assistant2);

        Optional<Assistant> retrieved = repository.get("testName1");
        assertTrue(retrieved.isPresent(), "Assistant testName1 should be present");
        assertEquals("testResponse1", retrieved.get().getResponse(), "Response for testName1 should match");
        
        retrieved = repository.get("testName2");
        assertTrue(retrieved.isPresent(), "Assistant testName2 should be present");
        assertEquals("testResponse2", retrieved.get().getResponse(), "Response for testName2 should match");
    }

    @Test
    void get_forNonExistentAssistant_shouldReturnEmptyOptional() {
        Optional<Assistant> retrieved = repository.get("nonExistentName");
        assertFalse(retrieved.isPresent(), "Optional should be empty for a non-existent assistant");
    }

    @Test
    void save_overwriteExistingAssistant() {
        Assistant assistant1 = new Assistant("testName", "initialResponse");
        repository.save(assistant1);

        Optional<Assistant> retrieved1 = repository.get("testName");
        assertTrue(retrieved1.isPresent());
        assertEquals("initialResponse", retrieved1.get().getResponse());

        Assistant assistant2 = new Assistant("testName", "updatedResponse");
        repository.save(assistant2);

        Optional<Assistant> retrieved2 = repository.get("testName");
        assertTrue(retrieved2.isPresent());
        assertEquals("updatedResponse", retrieved2.get().getResponse(), "Response should be updated");
        assertEquals("testName", retrieved2.get().getName(), "Name should remain the same");
    }
}
