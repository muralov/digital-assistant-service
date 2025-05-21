package com.home.assistant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.assistant.exception.AssistantNotFoundException;
import com.home.assistant.model.AssistantResponse;
import com.home.assistant.model.ChatRequest;
import com.home.assistant.repository.Assistant;
import com.home.assistant.repository.AssistantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssistantController.class)
public class AssistantControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssistantRepository assistantRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrUpdateAssistant_shouldReturnOk() throws Exception {
        AssistantResponse response = new AssistantResponse();
        response.setResponse("Hello");

        mockMvc.perform(put("/assistants/{name}", "testAssistant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(response)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(assistantRepository).save(any(Assistant.class));
    }

    @Test
    void createOrUpdateAssistant_whenBodyIsInvalid_shouldReturnBadRequest() throws Exception {
        AssistantResponse response = new AssistantResponse(); // Missing response field
        // response.setResponse(null); //This would be a way to explicitly test @NotBlank

        mockMvc.perform(put("/assistants/{name}", "testAssistant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(response)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void chat_shouldReturnFormattedMessage() throws Exception {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setMessage("Hi there");

        Assistant mockAssistant = new Assistant("testAssistant", "Stored response");
        when(assistantRepository.get("testAssistant")).thenReturn(Optional.of(mockAssistant));

        mockMvc.perform(post("/assistants/{name}/chat", "testAssistant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chatRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("You said: Hi there"));

        verify(assistantRepository).get("testAssistant");
    }

    @Test
    void chat_whenAssistantNotFound_shouldReturnNotFound() throws Exception {
        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setMessage("Hi there");

        when(assistantRepository.get("unknownAssistant")).thenReturn(Optional.empty());

        mockMvc.perform(post("/assistants/{name}/chat", "unknownAssistant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chatRequest)))
                .andExpect(status().isNotFound());
         verify(assistantRepository).get("unknownAssistant");
    }

    @Test
    void chat_whenMessageIsMissing_shouldReturnBadRequest() throws Exception {
        ChatRequest chatRequest = new ChatRequest(); // Message is null

        mockMvc.perform(post("/assistants/{name}/chat", "testAssistant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chatRequest)))
                .andExpect(status().isBadRequest());
    }
}
