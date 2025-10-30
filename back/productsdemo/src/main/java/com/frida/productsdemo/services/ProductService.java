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
 * Servicio de productos para operaciones CRUD completas.
 * 
 * Esta clase proporciona la lógica de negocio para el manejo de productos,
 * incluyendo operaciones de creación, lectura, actualización y eliminación.
 * Actúa como intermediario entre el controlador y la capa de persistencia,
 * manejando las conversiones entre entidades de dominio (Product) y DTOs
 * de persistencia (ProductDto).
 * 
 * Funcionalidades principales:
 * - Obtener todos los productos
 * - Obtener un producto por ID
 * - Crear nuevos productos
 * - Actualizar productos existentes
 * - Eliminar productos
 * - Análisis de productos mediante imágenes usando IA
 * 
 * Todas las operaciones incluyen validaciones de negocio apropiadas
 * y manejo de errores para garantizar la integridad de los datos.
 * 
 * @author Sistema de Productos
 * @version 1.0
 * @since 2024
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

    /**
     * Analiza una imagen de producto utilizando inteligencia artificial.
     * 
     * Este método procesa una imagen enviada como archivo multipart y utiliza
     * el servicio de IA de Frida para extraer información del producto como
     * referencia, nombre, marca, descripción, precio, stock y departamento.
     * 
     * @param file La imagen del producto a analizar (formato JPEG, PNG, etc.)
     * @return Un objeto Product con los datos extraídos de la imagen, o null si hay error
     * @throws RuntimeException si ocurre un error durante el procesamiento de la imagen
     */
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

	/**
	 * Obtiene la lista completa de todos los productos.
	 * 
	 * Recupera todos los productos almacenados en la base de datos
	 * y los convierte a objetos de dominio para su uso en la aplicación.
	 * 
	 * @return Lista de todos los productos disponibles. Lista vacía si no hay productos.
	 */
	public List<Product> getAllProducts() {
		List<ProductDto> entities = productRepository.findAll();
		return productMapper.toEntityList(entities);
		
	}
	
	/**
	 * Obtiene un producto específico por su ID.
	 * 
	 * Busca un producto en la base de datos utilizando su identificador único.
	 * Si el producto no existe, retorna null.
	 * 
	 * @param id El identificador único del producto a buscar
	 * @return El producto encontrado o null si no existe
	 */
	public Product getProductById(Long id) {
		// Buscar el producto por ID en el repositorio
		ProductDto productDto = productRepository.findById(id).orElse(null);
		
		if (productDto == null) {
			return null;
		}
		
		// Convertir ProductDto a Product y retornar
		return productMapper.toEntity(productDto);
	}

	/**
	 * Crea un nuevo producto en la base de datos.
	 * 
	 * Persiste un nuevo producto en la base de datos después de realizar
	 * las conversiones necesarias entre objetos de dominio y DTOs.
	 * El ID del producto será asignado automáticamente por la base de datos.
	 * 
	 * @param product El producto a crear con todos sus datos requeridos
	 * @return El producto creado con su ID asignado por la base de datos
	 * @throws RuntimeException si ocurre un error durante la persistencia
	 */
	public Product createProduct(Product product) {
		// Convertir Product a ProductDto para persistir
		ProductDto productDto = productMapper.toDto(product);
		
		// Guardar en la base de datos
		ProductDto savedDto = productRepository.save(productDto);
		
		// Convertir de vuelta a Product y retornar
		return productMapper.toEntity(savedDto);
	}

	/**
	 * Actualiza un producto existente en la base de datos.
	 * 
	 * Busca el producto por su ID y actualiza todos sus campos con los nuevos valores.
	 * Si el producto no existe, retorna null.
	 * 
	 * @param id El identificador único del producto a actualizar
	 * @param product Los nuevos datos del producto
	 * @return El producto actualizado o null si no existe
	 */
	public Product updateProduct(Long id, Product product) {
		// Verificar si el producto existe
		ProductDto existingDto = productRepository.findById(id).orElse(null);
		
		if (existingDto == null) {
			return null;
		}
		
		// Convertir el nuevo producto a DTO
		ProductDto updatedDto = productMapper.toDto(product);
		
		// Asignar el ID del producto existente
		updatedDto.setId(id);
		
		// Guardar los cambios en la base de datos
		ProductDto savedDto = productRepository.save(updatedDto);
		
		// Convertir de vuelta a Product y retornar
		return productMapper.toEntity(savedDto);
	}

	/**
	 * Elimina un producto de la base de datos.
	 * 
	 * Busca el producto por su ID y lo elimina si existe.
	 * 
	 * @param id El identificador único del producto a eliminar
	 * @return true si el producto fue eliminado, false si no existía
	 */
	public boolean deleteProduct(Long id) {
		// Verificar si el producto existe
		if (!productRepository.existsById(id)) {
			return false;
		}
		
		// Eliminar el producto
		productRepository.deleteById(id);
		
		return true;
	}
	
	/**
	 * Extrae el primer fragmento JSON válido de una cadena de texto.
	 * 
	 * Método utilitario que busca y extrae el primer objeto JSON o array JSON
	 * completo encontrado en un texto. Realiza balanceo de llaves/corchetes
	 * para asegurar que el JSON extraído esté bien formado.
	 * 
	 * @param text La cadena de texto que contiene JSON embebido
	 * @return El fragmento JSON extraído como cadena
	 * @throws IllegalArgumentException si no se encuentra JSON válido o está mal formado
	 */
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
