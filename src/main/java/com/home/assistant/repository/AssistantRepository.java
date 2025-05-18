package com.home.assistant.repository;

import java.util.Optional;

import com.home.assistant.model.Assistant;

public interface AssistantRepository {
    Assistant create(Assistant assistant);
    Assistant update(Assistant assistant);
    Optional<Assistant> get(String name);
} 