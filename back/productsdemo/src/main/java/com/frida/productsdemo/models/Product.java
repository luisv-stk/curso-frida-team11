package com.frida.productsdemo.models;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * Clase básica que representa un producto.
 * No usa JPA ni persistencia, ideal para lógica o pruebas simples.
 */
@Getter
@Setter
public class Product {
    private String referencia;
    private String nombre;
    private String marca;
    private String descripcion;
    private BigDecimal precio; // Usar BigDecimal para manejar dinero
    private int numeroDisponible;
    private String departamento;

    public Product() {}
    
    /**
     * Constructor completo para todos los campos.
     * 
     * @param referencia Código único de producto.
     * @param nombre Nombre del producto.
     * @param marca Marca del producto.
     * @param descripcion Descripción del producto.
     * @param precio Precio del producto (BigDecimal).
     * @param numeroDisponible Stock disponible.
     * @param departamento Departamento/categoría.
     */
    public Product(String referencia, String nombre, String marca, String descripcion,
                   BigDecimal precio, int numeroDisponible, String departamento) {
        this.referencia = referencia;
        this.nombre = nombre;
        this.marca = marca;
        this.descripcion = descripcion;
        this.precio = precio;
        this.numeroDisponible = numeroDisponible;
        this.departamento = departamento;
    }


}