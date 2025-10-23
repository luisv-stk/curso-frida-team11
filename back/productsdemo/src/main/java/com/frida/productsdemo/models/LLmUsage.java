// Usage.java
package com.frida.productsdemo.models;

public class LLmUsage {
    private int prompt_tokens;
    private int completion_tokens;
    private int total_tokens;
    private int cache_read_input_tokens;
    private int cache_write_input_tokens;

    // Getters y setters
    public int getPrompt_tokens() { return prompt_tokens; }
    public void setPrompt_tokens(int prompt_tokens) { this.prompt_tokens = prompt_tokens; }
    public int getCompletion_tokens() { return completion_tokens; }
    public void setCompletion_tokens(int completion_tokens) { this.completion_tokens = completion_tokens; }
    public int getTotal_tokens() { return total_tokens; }
    public void setTotal_tokens(int total_tokens) { this.total_tokens = total_tokens; }
    public int getCache_read_input_tokens() { return cache_read_input_tokens; }
    public void setCache_read_input_tokens(int cache_read_input_tokens) { this.cache_read_input_tokens = cache_read_input_tokens; }
    public int getCache_write_input_tokens() { return cache_write_input_tokens; }
    public void setCache_write_input_tokens(int cache_write_input_tokens) { this.cache_write_input_tokens = cache_write_input_tokens; }
}