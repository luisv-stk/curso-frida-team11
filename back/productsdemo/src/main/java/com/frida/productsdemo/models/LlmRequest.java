// File: LlmRequest.java

package com.frida.productsdemo.models;

import java.util.List;

/**
 * Represents a request body for LLM chat completion endpoint
 */
public class LlmRequest {
    private String model;
    private List<LlmMessage> messages;
    private boolean stream;
    private boolean enable_catching;

    public LlmRequest() {}

    public LlmRequest(String model, List<LlmMessage> messages, boolean stream, boolean enable_catching) {
        this.model = model;
        this.messages = messages;
        this.stream = stream;
        this.enable_catching = enable_catching;
    }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public List<LlmMessage> getMessages() { return messages; }
    public void setMessages(List<LlmMessage> messages) { this.messages = messages; }

    public boolean isStream() { return stream; }
    public void setStream(boolean stream) { this.stream = stream; }

    public boolean isEnable_catching() { return enable_catching; }
    public void setEnable_catching(boolean enable_catching) { this.enable_catching = enable_catching; }
}