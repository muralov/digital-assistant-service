package com.home.assistant.model;

import jakarta.validation.constraints.NotBlank;

public class AssistantResponse {
    
    @NotBlank(message = "response field is required")
    private String response;

    // Constructor
    public AssistantResponse() {}

    // Getters and Setters
    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}