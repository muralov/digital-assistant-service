package com.home.assistant.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository("inMemoryRepository")
public class InMemoryRepository implements AssistantRepository {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(InMemoryRepository.class);
    private final Map<String, String> assistantResponses = new ConcurrentHashMap<>();

    @Override
    public Assistant save(Assistant assistant) {
        assistantResponses.put(assistant.getName(), assistant.getResponse());
        logger.info("digital assistant '{}' saved in memory", assistant.getName());
        return assistant;
    }
    
    @Override
    public Optional<Assistant> get(String name) {
        return Optional.ofNullable(assistantResponses.get(name))
                .map(response -> new Assistant(name, response));
    }
    
} 