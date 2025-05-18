package com.home.assistant.repository;

import java.util.Optional;

public interface AssistantRepository {
    Assistant save(Assistant assistant);
    Optional<Assistant> get(String name);
} 