package com.home.assistant.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.home.assistant.exception.AssistantNotFoundException;
import com.home.assistant.model.Assistant;
import com.home.assistant.model.ChatRequest;
import com.home.assistant.repository.AssistantRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/assistants")
public class AssistantController {

    private final static Logger logger = LoggerFactory.getLogger(AssistantController.class);

    
    private final AssistantRepository assistantRepository;

    public AssistantController(AssistantRepository assistantRepository) {
        this.assistantRepository = assistantRepository;
    }

    @PostMapping
    public Assistant createAssistant(@Valid @RequestBody Assistant newAssistant) {
        return assistantRepository.create(newAssistant);
    }

    @PutMapping("/{name}")
    public Assistant updateAssistantResponse(@PathVariable String name, @Valid @RequestBody String newResponse) {
        return assistantRepository.update(new Assistant(name, newResponse));
    }

    @PostMapping("/{name}/chat")
    public Assistant chat(@PathVariable String name, @Valid @RequestBody ChatRequest request) {
        return assistantRepository.get(name).orElseThrow(() -> {
            logger.error("Assistant '{}' not found", name);
            return new AssistantNotFoundException(name);
        });
    }
}