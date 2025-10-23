package com.frida.productsdemo.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.frida.productsdemo.models.Product;
import com.frida.productsdemo.services.ProductService;

/**
 * Controlador REST para manejo de productos.
 * Permite obtener la lista de todos los productos y crear uno nuevo.
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
     * Crea un nuevo producto.
     * @param product Producto recibido en el cuerpo de la solicitud.
     * @return El producto creado.
     */

    @PostMapping("/new")
    public String uploadProductFile(@RequestParam("file") MultipartFile file) throws IOException {
        // Solo para prueba: muestra nombre y tamaño del archivo
        String message = "Archivo recibido: " + file.getOriginalFilename() +
                         ", tamaño: " + file.getSize() + " bytes";
        
        service.producEvaluation(file);
        

        return message;
    }
}