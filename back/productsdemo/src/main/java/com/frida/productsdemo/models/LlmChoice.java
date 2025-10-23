// Choice.java
package com.frida.productsdemo.models;

public class LlmChoice {
    private int index;
    private LlmResponseMessage message;
    private String finish_reason;

    // Getters y setters
    public int getIndex() { return index; }
    public void setIndex(int index) { this.index = index; }
    public LlmResponseMessage getMessage() { return message; }
    public void setMessage(LlmResponseMessage message) { this.message = message; }
    public String getFinish_reason() { return finish_reason; }
    public void setFinish_reason(String finish_reason) { this.finish_reason = finish_reason; }
}