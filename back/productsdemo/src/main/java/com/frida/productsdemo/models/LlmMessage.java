// File: LlmMessage.java

package com.frida.productsdemo.models;

import java.util.List;

/**
 * Represents a message to send to the LLM API
 */
public class LlmMessage {
    private String role;
    private List<LlmContent> content;

    public LlmMessage() {}

    public LlmMessage(String role, List<LlmContent> content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<LlmContent> getContent() { return content; }
    public void setContent(List<LlmContent> content) { this.content = content; }
}