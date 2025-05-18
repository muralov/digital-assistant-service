package com.home.assistant.model;

import jakarta.validation.constraints.NotBlank;

public class ChatRequest {
    @NotBlank(message = "message field is required")
    private String message;

    // Constructor
    public ChatRequest() {}
    
    // Getter and Setter
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}