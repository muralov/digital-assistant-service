package com.home.assistant.repository;

public class Assistant {
    private String name;
    private String response;

    public Assistant(String name, String response) {
        this.name = name;
        this.response = response;
    }

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