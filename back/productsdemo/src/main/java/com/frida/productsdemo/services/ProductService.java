package com.frida.productsdemo.services;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

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
import com.google.gson.Gson;

/**
 * Servicio de productos para operaciones relacionadas con productos.
 */
@Component
public class ProductService {

	private static final String PROMPT_IMAGEN = "Analiza la imagen, dame la respuesta en un json que tenga los datos referencia, nombre, marca, descripcion, precio, numeroDisponible y departamento. " +
			"Usa una referencia con la marca y algo corto. Precio y numero disponible son enteros, en caso de no poder saberlo pon 1";

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
    	
    	
    	LlmContent text = LlmContent.ofText(PROMPT_IMAGEN);
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
    	
    	
		String fragment = extractJsonFragment(response.getChoices().get(0).getMessage().getContent());
		Product obj = new Gson().fromJson(fragment, Product.class);

    	return obj;
    	
    }

	public List<Product> getAllProducts() {
		List<ProductDto> entities = productRepository.findAll();
		return productMapper.toEntityList(entities);
		
	}
	
	/**
	 * Crea un nuevo producto en la base de datos.
	 * 
	 * @param product El producto a crear
	 * @return El producto creado con su ID asignado
	 */
	public Product createProduct(Product product) {
		// Convertir Product a ProductDto para persistir
		ProductDto productDto = productMapper.toDto(product);
		
		// Guardar en la base de datos
		ProductDto savedDto = productRepository.save(productDto);
		
		// Convertir de vuelta a Product y retornar
		return productMapper.toEntity(savedDto);
	}
	
	// Extrae substring del primer objeto/array JSON haciendo balanceo de llaves/corchetes
	public static String extractJsonFragment(String text) {
	    int start = -1;
	    char open = 0;
	    for (int i = 0; i < text.length(); i++) {
	        char c = text.charAt(i);
	        if (c == '{' || c == '[') { start = i; open = c; break; }
	    }
	    if (start == -1) throw new IllegalArgumentException("No se encontró apertura JSON");

	    char close = open == '{' ? '}' : ']';
	    int depth = 0;
	    boolean inString = false;
	    for (int i = start; i < text.length(); i++) {
	        char c = text.charAt(i);
	        if (c == '"' && (i == 0 || text.charAt(i - 1) != '\\')) inString = !inString;
	        if (inString) continue;
	        if (c == open) depth++;
	        else if (c == close) {
	            depth--;
	            if (depth == 0) return text.substring(start, i + 1);
	        }
	    }
	    throw new IllegalArgumentException("JSON incompleto o no balanceado");
	}
	
	
	
}
