// File: LlmContent.java

package com.frida.productsdemo.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Represents content in an LLM message; can be text or document
 */
public class LlmContent {
   
	private String type; // "text" or "document"
    
	@JsonInclude(Include.NON_NULL)
	private String text;
    
	@JsonInclude(Include.NON_NULL)
    private LlmDocument image_url;

    public LlmContent() {}

    // For text
    public static LlmContent ofText(String text) {
        LlmContent content = new LlmContent();
        content.type = "text";
        content.text = text;
        return content;
    }

    // For document
    public static LlmContent ofImage(LlmDocument image_url) {
        LlmContent content = new LlmContent();
        content.type = "image_url";
        content.image_url = image_url;
        return content;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public LlmDocument getImage_url() { return image_url; }
    public void setImage_url(LlmDocument document) { this.image_url = document; }
}