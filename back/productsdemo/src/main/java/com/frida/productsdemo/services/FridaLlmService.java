// File: FridaLlmService.java

package com.frida.productsdemo.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.frida.productsdemo.models.LlmRequest;
import com.frida.productsdemo.models.LlmResponse;

import java.util.List;

/**
 * Service to interact with Frida LLM API for chat completions
 */
@Service
public class FridaLlmService {

    @Value("${llm-url}")
    private String llmBaseUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * Calls Frida LLM chat completion endpoint with provided request
     *
     * @param request Chat completion request payload
     * @return Chat completion response
     */
    public LlmResponse callChatCompletion(LlmRequest request) {
        
    	String endpoint = llmBaseUrl + "/v1/chat/completions";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth("nA6EgISiPUzsaGMKufDG");
        HttpEntity<LlmRequest> entity = new HttpEntity<>(request, headers);
        
     // SERIALIZA y muestra el JSON que realmente se envía
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonRequest = mapper.writeValueAsString(request);
            // System.out.println("JSON enviado a Frida LLM:\n" + jsonRequest);
        } catch (Exception e) {
            System.err.println("Error al serializar LlmRequest: " + e.getMessage());
        }

        ResponseEntity<LlmResponse> response = restTemplate.exchange(
            endpoint,
            HttpMethod.POST,
            entity,
            LlmResponse.class
        );
        
        

        return response.getBody();
    }
}