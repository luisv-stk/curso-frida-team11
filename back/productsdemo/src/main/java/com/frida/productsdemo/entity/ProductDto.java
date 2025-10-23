package com.frida.productsdemo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.math.BigDecimal;

import jakarta.persistence.Column;

/**
 * Entidad que representa un producto.
 */
@Entity
public class ProductDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Este campo es opcional, añadido para la base de datos

    @Column(nullable = false, unique = true)
    private String referencia;

    @Column(nullable = false)
    private String nombre;

    private String marca;
    private String descripcion;
    private BigDecimal precio; // Considera usar BigDecimal para precisión monetaria

    private int numeroDisponible;
    private String departamento;

    public ProductDto() {}

    public ProductDto(String referencia, String nombre, String marca, String descripcion,
    		BigDecimal precio, int numeroDisponible, String departamento) {
        this.referencia = referencia;
        this.nombre = nombre;
        this.marca = marca;
        this.descripcion = descripcion;
        this.precio = precio;
        this.numeroDisponible = numeroDisponible;
        this.departamento = departamento;
    }

    // Getters y setters
    public Long getId() { return id; }
    public String getReferencia() { return referencia; }
    public String getNombre() { return nombre; }
    public String getMarca() { return marca; }
    public String getDescripcion() { return descripcion; }
    public BigDecimal getPrecio() { return precio; }
    public int getNumeroDisponible() { return numeroDisponible; }
    public String getDepartamento() { return departamento; }

    public void setId(Long id) { this.id = id; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setMarca(String marca) { this.marca = marca; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public void setNumeroDisponible(int numeroDisponible) { this.numeroDisponible = numeroDisponible; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
}