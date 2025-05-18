package com.home.assistant.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.home.assistant.exception.AssistantExistsException;
import com.home.assistant.model.Assistant;

@Repository("inMemoryRepository")
public class InMemoryRepository implements AssistantRepository {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(InMemoryRepository.class);
    private final Map<String, String> assistantResponses = new ConcurrentHashMap<>();

    @Override
    public Assistant create(Assistant assistant) {
        // Check if the assistant already exists
        String value = assistantResponses.putIfAbsent(assistant.getName(), assistant.getResponse());
        if (value == null) {
            logger.info("digital assistant created in memory: {}", assistant.getName());
            return assistant;
        } else {
            logger.error("Assistant '" + assistant.getName() + "' already exists");
            throw new AssistantExistsException("Assistant '" + assistant.getName() + "' already exists");
        }
    }
    
    @Override
    public Assistant update(Assistant assistant) {
        assistantResponses.put(assistant.getName(), assistant.getResponse());
        logger.info("digital assistant saved in memory: {}", assistant.getName());
        return assistant;
    }
    
    @Override
    public Optional<Assistant> get(String name) {
        return Optional.ofNullable(assistantResponses.get(name))
                .map(response -> new Assistant(name, response));
    }
    
} 