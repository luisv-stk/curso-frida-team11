// File: LlmDocument.java

package com.frida.productsdemo.models;

/**
 * Represents a document to send to the LLM API
 */
public class LlmDocument {
    private String detail;
    private String url;

    public LlmDocument() {}

    public LlmDocument(String detail, String url) {
        this.detail = detail;
        this.url = url;
    }

    public String getDetail() { return detail; }
    public void setDetail(String name) { this.detail = name; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}