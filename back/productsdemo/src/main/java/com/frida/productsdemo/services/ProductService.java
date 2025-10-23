package com.frida.productsdemo.services;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.frida.productsdemo.entity.ProductDto;
import com.frida.productsdemo.models.LlmContent;
import com.frida.productsdemo.models.LlmDocument;
import com.frida.productsdemo.models.LlmMessage;
import com.frida.productsdemo.models.LlmRequest;
import com.frida.productsdemo.models.LlmResponse;
import com.frida.productsdemo.models.Product;
import com.frida.productsdemo.repository.ProductRepository;
import com.frida.productsdemo.services.frida.ModelConstants;

/**
 * Servicio de productos para operaciones relacionadas con productos.
 */
@Component
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private ProductMapper productMapper;
	
	@Autowired
	private FridaLlmService fridaService;

    public Product producEvaluation(MultipartFile file) {
        
    	
    	String base64 = null;
		try {
            byte[] fileBytes = file.getBytes();
            base64 = Base64.getEncoder().encodeToString(fileBytes);
       
		} catch (Exception e) {
            
            e.printStackTrace();
            return null;
        }
    	
    	LlmRequest req = new LlmRequest();
    	req.setModel(ModelConstants.CLAUDE_4_SONNET);
    	
    	List<LlmMessage> messageArray = new ArrayList<>();
    	
    	
    	
    	
    	LlmMessage msg = new LlmMessage();
    	msg.setRole("user");
    	List<LlmContent> contentArray = new ArrayList<>();
    	
    	
    	LlmContent text = LlmContent.ofText("Describe lo que hay en la imagen");
    	
    	LlmDocument doc = new LlmDocument();
    	doc.setDetail("auto");
    	doc.setUrl("data:image/jpeg;base64," + base64);
		
    	LlmContent image = LlmContent.ofImage(doc);
    	
		contentArray.add(image);
		contentArray.add(text);
		msg.setContent(contentArray);
    	messageArray.add(msg);
		
		req.setMessages(messageArray);
    	req.setStream(false);
    	req.setEnable_catching(true);
    	
    	
		LlmResponse response = fridaService.callChatCompletion(req);
    	
    	
    	
    	
    	return new Product();
    	
    }

	public List<Product> getAllProducts() {
		List<ProductDto> entities = productRepository.findAll();
		return productMapper.toEntityList(entities);
		
	}
}
