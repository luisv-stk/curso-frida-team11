// File: LlmResponse.java

package com.frida.productsdemo.models;

import java.util.List;

public class LlmResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<LlmChoice> choices;
    private LLmUsage usage;

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getObject() { return object; }
    public void setObject(String object) { this.object = object; }
    
    public long getCreated() { return created; }
    public void setCreated(long created) { this.created = created; }
    
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    
    public List<LlmChoice> getChoices() { return choices; }
    public void setChoices(List<LlmChoice> choices) { this.choices = choices; }
    
    public LLmUsage getUsage() { return usage; }
    public void setUsage(LLmUsage usage) { this.usage = usage; }
}