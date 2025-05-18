package com.home.assistant.model;

import jakarta.validation.constraints.NotBlank;

public class Assistant {
    @NotBlank(message = "Name is required")
    private String name;
    
    @NotBlank(message = "Response is required")
    private String response;

    // Constructor
    public Assistant(String name, String response) {
        this.name = name;
        this.response = response;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}