package com.frida.productsdemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.frida.productsdemo.entity.ProductDto;

/**
 * Repositorio para la entidad Product.
 * Provee operaciones CRUD y consultas personalizadas si se requieren.
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductDto, Long> {
    // Puedes agregar métodos personalizados aquí, ejemplo:
    // Optional<Product> findByReferencia(String referencia);
}