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
import com.home.assistant.model.AssistantResponse;
import com.home.assistant.model.ChatRequest;
import com.home.assistant.repository.Assistant;
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

    @PutMapping("/{name}")
    public AssistantResponse createOrUpdateAssistant(@PathVariable String name, @Valid @RequestBody AssistantResponse assistantResponse) {
        assistantRepository.save(new Assistant(name, assistantResponse.getResponse()));
        return assistantResponse;
    }

    @PostMapping("/{name}/chat")
    public String chat(@PathVariable String name, @Valid @RequestBody ChatRequest request) {
        Assistant assistant = assistantRepository.get(name).orElseThrow(() -> {
            logger.error("Assistant '{}' not found", name);
            return new AssistantNotFoundException(name);
        });
        
        // Return the formatted message
        return "You said: " + request.getMessage();
    }
}