package com.frida.productsdemo.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.frida.productsdemo.models.Product;
import com.frida.productsdemo.services.ProductService;

/**
 * Controlador REST para manejo completo de productos.
 * 
 * Proporciona endpoints para operaciones CRUD completas sobre productos:
 * - GET /api/products - Obtener lista de todos los productos
 * - GET /api/products/{id} - Obtener un producto específico por ID
 * - POST /api/products/new - Crear un nuevo producto
 * - PUT /api/products/{id} - Actualizar un producto existente
 * - DELETE /api/products/{id} - Eliminar un producto
 * - POST /api/products/analize - Analizar producto desde archivo
 * 
 * Todos los endpoints implementan validación completa de datos de entrada,
 * manejo de errores consistente y respuestas HTTP apropiadas.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService service;

    /**
     * Obtiene la lista de todos los productos.
     * @return Lista de productos.
     */
    @GetMapping
    public List<Product> getAllProducts() {
        List<Product> products = service.getAllProducts();
        return products;
    }

    /**
     * Obtiene un producto específico por su ID.
     * 
     * Este endpoint permite recuperar los datos completos de un producto
     * específico mediante su identificador único. Es útil para operaciones
     * de consulta individual y como paso previo para editar un producto.
     * 
     * @param id El identificador único del producto a recuperar
     * 
     * @return ResponseEntity que contiene:
     *         - 200 OK: Producto encontrado con sus datos completos
     *         - 404 NOT FOUND: Producto no encontrado con el ID especificado
     *         - 400 BAD REQUEST: ID inválido (no numérico o negativo)
     *         - 500 INTERNAL SERVER ERROR: Error interno del servidor
     * 
     * @apiNote Ejemplo de uso:
     *          GET /api/products/123
     *          
     *          Respuesta exitosa:
     *          {
     *            "id": 123,
     *            "referencia": "REF001",
     *            "nombre": "Producto Ejemplo",
     *            "marca": "MarcaEjemplo",
     *            "precio": 29.99,
     *            "numeroDisponible": 100,
     *            "departamento": "Electrónicos",
     *            "descripcion": "Descripción del producto"
     *          }
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            // Validación del ID
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body("Error: El ID del producto debe ser un número positivo válido");
            }
            
            // Buscar el producto por ID usando el servicio
            Product product = service.getProductById(id);
            
            if (product == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró un producto con el ID especificado: " + id);
            }
            
            return ResponseEntity.ok(product);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + e.getMessage());
        }
    }

    /**
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/analize")
    public Product uploadProductFile(@RequestParam("file") MultipartFile file) throws IOException {
    	Product product = service.producEvaluation(file);
    	return product;
    }

    /**
     * Crea un nuevo producto en el sistema.
     * 
     * Este endpoint permite dar de alta un nuevo producto proporcionando los datos
     * necesarios en formato JSON. Realiza validaciones de los campos requeridos
     * y devuelve el producto creado con su ID asignado.
     * 
     * @param product El producto a crear. Debe incluir:
     *                - referencia: Código único del producto (requerido, no vacío, máximo 50 caracteres)
     *                - nombre: Nombre del producto (requerido, no vacío, máximo 255 caracteres)
     *                - marca: Marca del producto (requerido, no vacío, máximo 100 caracteres)
     *                - precio: Precio del producto (requerido, mayor a 0, máximo $999,999.99)
     *                - numeroDisponible: Stock disponible (requerido, mayor o igual a 0)
     *                - departamento: Departamento/categoría (requerido, no vacío, máximo 100 caracteres)
     *                - descripcion: Descripción del producto (opcional, máximo 1000 caracteres)
     * 
     * @return ResponseEntity que contiene:
     *         - 201 CREATED: Producto creado exitosamente con los datos del nuevo producto
     *         - 400 BAD REQUEST: Error de validación con mensaje descriptivo
     *         - 500 INTERNAL SERVER ERROR: Error interno del servidor
     * 
     * @apiNote Ejemplo de uso:
     *          POST /api/products/new
     *          Content-Type: application/json
     *          
     *          {
     *            "referencia": "REF001",
     *            "nombre": "Producto Ejemplo",
     *            "marca": "MarcaEjemplo", 
     *            "precio": 29.99,
     *            "numeroDisponible": 100,
     *            "departamento": "Electrónicos",
     *            "descripcion": "Descripción del producto"
     *          }
     */
    @PostMapping("/new")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        try {
            // Validación básica de datos requeridos
            if (product == null) {
                return ResponseEntity.badRequest()
                    .body("Error: Los datos del producto son requeridos");
            }
            
            // Validación del nombre (campo requerido)
            if (product.getNombre() == null || product.getNombre().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body("Error: El nombre del producto es requerido");
            }
            
            // Validación del precio (campo requerido, debe ser mayor a 0)
            if (product.getPrecio() == null || product.getPrecio().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest()
                    .body("Error: El precio del producto debe ser mayor a 0");
            }
            
            // Validación de la referencia (campo requerido)
            if (product.getReferencia() == null || product.getReferencia().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body("Error: La referencia del producto es requerida");
            }
            
            // Validación de la marca (campo requerido)
            if (product.getMarca() == null || product.getMarca().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body("Error: La marca del producto es requerida");
            }
            
            // Validación del departamento (campo requerido)
            if (product.getDepartamento() == null || product.getDepartamento().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body("Error: El departamento del producto es requerido");
            }
            
            // Validación del número disponible (debe ser mayor o igual a 0)
            if (product.getNumeroDisponible() < 0) {
                return ResponseEntity.badRequest()
                    .body("Error: El número disponible del producto no puede ser negativo");
            }
            
            // Validación de la descripción (opcional pero si se proporciona no debe estar vacía)
            if (product.getDescripcion() != null && product.getDescripcion().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body("Error: La descripción del producto no puede estar vacía");
            }
            
            // Validaciones de formato y longitud
            if (product.getNombre().length() > 255) {
                return ResponseEntity.badRequest()
                    .body("Error: El nombre del producto no puede exceder 255 caracteres");
            }
            
            if (product.getReferencia().length() > 50) {
                return ResponseEntity.badRequest()
                    .body("Error: La referencia del producto no puede exceder 50 caracteres");
            }
            
            if (product.getMarca().length() > 100) {
                return ResponseEntity.badRequest()
                    .body("Error: La marca del producto no puede exceder 100 caracteres");
            }
            
            if (product.getDepartamento().length() > 100) {
                return ResponseEntity.badRequest()
                    .body("Error: El departamento del producto no puede exceder 100 caracteres");
            }
            
            if (product.getDescripcion() != null && product.getDescripcion().length() > 1000) {
                return ResponseEntity.badRequest()
                    .body("Error: La descripción del producto no puede exceder 1000 caracteres");
            }
            
            // Validación del precio máximo razonable
            BigDecimal maxPrice = new BigDecimal("999999.99");
            if (product.getPrecio().compareTo(maxPrice) > 0) {
                return ResponseEntity.badRequest()
                    .body("Error: El precio del producto no puede exceder $999,999.99");
            }
            
            // Crear el producto usando el servicio
            Product newProduct = service.createProduct(product);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + e.getMessage());
        }
    }

    /**
     * Actualiza un producto existente en el sistema.
     * 
     * Este endpoint permite modificar los datos de un producto existente
     * mediante su ID. Realiza las mismas validaciones que la creación
     * y devuelve el producto actualizado.
     * 
     * @param id El identificador único del producto a actualizar
     * @param product Los nuevos datos del producto. Debe incluir los mismos campos que en creación:
     *                - referencia: Código único del producto (requerido, no vacío, máximo 50 caracteres)
     *                - nombre: Nombre del producto (requerido, no vacío, máximo 255 caracteres)
     *                - marca: Marca del producto (requerido, no vacío, máximo 100 caracteres)
     *                - precio: Precio del producto (requerido, mayor a 0, máximo $999,999.99)
     *                - numeroDisponible: Stock disponible (requerido, mayor o igual a 0)
     *                - departamento: Departamento/categoría (requerido, no vacío, máximo 100 caracteres)
     *                - descripcion: Descripción del producto (opcional, máximo 1000 caracteres)
     * 
     * @return ResponseEntity que contiene:
     *         - 200 OK: Producto actualizado exitosamente con los nuevos datos
     *         - 404 NOT FOUND: Producto no encontrado con el ID especificado
     *         - 400 BAD REQUEST: Error de validación con mensaje descriptivo o ID inválido
     *         - 500 INTERNAL SERVER ERROR: Error interno del servidor
     * 
     * @apiNote Ejemplo de uso:
     *          PUT /api/products/123
     *          Content-Type: application/json
     *          
     *          {
     *            "referencia": "REF001-UPD",
     *            "nombre": "Producto Actualizado",
     *            "marca": "MarcaNueva",
     *            "precio": 35.99,
     *            "numeroDisponible": 85,
     *            "departamento": "Electrónicos",
     *            "descripcion": "Nueva descripción del producto"
     *          }
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        try {
            // Validación del ID
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body("Error: El ID del producto debe ser un número positivo válido");
            }
            
            // Validación básica de datos requeridos
            if (product == null) {
                return ResponseEntity.badRequest()
                    .body("Error: Los datos del producto son requeridos");
            }
            
            // Validación del nombre (campo requerido)
            if (product.getNombre() == null || product.getNombre().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body("Error: El nombre del producto es requerido");
            }
            
            // Validación del precio (campo requerido, debe ser mayor a 0)
            if (product.getPrecio() == null || product.getPrecio().compareTo(java.math.BigDecimal.ZERO) <= 0) {
                return ResponseEntity.badRequest()
                    .body("Error: El precio del producto debe ser mayor a 0");
            }
            
            // Validación de la referencia (campo requerido)
            if (product.getReferencia() == null || product.getReferencia().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body("Error: La referencia del producto es requerida");
            }
            
            // Validación de la marca (campo requerido)
            if (product.getMarca() == null || product.getMarca().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body("Error: La marca del producto es requerida");
            }
            
            // Validación del departamento (campo requerido)
            if (product.getDepartamento() == null || product.getDepartamento().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body("Error: El departamento del producto es requerido");
            }
            
            // Validación del número disponible (debe ser mayor o igual a 0)
            if (product.getNumeroDisponible() < 0) {
                return ResponseEntity.badRequest()
                    .body("Error: El número disponible del producto no puede ser negativo");
            }
            
            // Validación de la descripción (opcional pero si se proporciona no debe estar vacía)
            if (product.getDescripcion() != null && product.getDescripcion().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                    .body("Error: La descripción del producto no puede estar vacía");
            }
            
            // Validaciones de formato y longitud
            if (product.getNombre().length() > 255) {
                return ResponseEntity.badRequest()
                    .body("Error: El nombre del producto no puede exceder 255 caracteres");
            }
            
            if (product.getReferencia().length() > 50) {
                return ResponseEntity.badRequest()
                    .body("Error: La referencia del producto no puede exceder 50 caracteres");
            }
            
            if (product.getMarca().length() > 100) {
                return ResponseEntity.badRequest()
                    .body("Error: La marca del producto no puede exceder 100 caracteres");
            }
            
            if (product.getDepartamento().length() > 100) {
                return ResponseEntity.badRequest()
                    .body("Error: El departamento del producto no puede exceder 100 caracteres");
            }
            
            if (product.getDescripcion() != null && product.getDescripcion().length() > 1000) {
                return ResponseEntity.badRequest()
                    .body("Error: La descripción del producto no puede exceder 1000 caracteres");
            }
            
            // Validación del precio máximo razonable
            BigDecimal maxPrice = new BigDecimal("999999.99");
            if (product.getPrecio().compareTo(maxPrice) > 0) {
                return ResponseEntity.badRequest()
                    .body("Error: El precio del producto no puede exceder $999,999.99");
            }
            
            // Actualizar el producto usando el servicio
            Product updatedProduct = service.updateProduct(id, product);
            
            if (updatedProduct == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró un producto con el ID especificado: " + id);
            }
            
            return ResponseEntity.ok(updatedProduct);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + e.getMessage());
        }
    }

    /**
     * Elimina un producto del sistema.
     * 
     * Este endpoint permite eliminar permanentemente un producto específico
     * mediante su ID. Una vez eliminado, el producto no podrá ser recuperado.
     * Se recomienda usar con precaución en entornos de producción.
     * 
     * @param id El identificador único del producto a eliminar
     * 
     * @return ResponseEntity que contiene:
     *         - 200 OK: Producto eliminado exitosamente con mensaje de confirmación
     *         - 404 NOT FOUND: Producto no encontrado con el ID especificado
     *         - 400 BAD REQUEST: ID inválido (no numérico o negativo)
     *         - 500 INTERNAL SERVER ERROR: Error interno del servidor
     * 
     * @apiNote Ejemplo de uso:
     *          DELETE /api/products/123
     *          
     *          Respuesta exitosa:
     *          {
     *            "message": "Producto con ID 123 eliminado exitosamente"
     *          }
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            // Validación del ID
            if (id == null || id <= 0) {
                return ResponseEntity.badRequest()
                    .body("Error: El ID del producto debe ser un número positivo válido");
            }
            
            // Eliminar el producto usando el servicio
            boolean deleted = service.deleteProduct(id);
            
            if (!deleted) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Error: No se encontró un producto con el ID especificado: " + id);
            }
            
            return ResponseEntity.ok()
                .body("Producto con ID " + id + " eliminado exitosamente");
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error interno del servidor: " + e.getMessage());
        }
    }
}
