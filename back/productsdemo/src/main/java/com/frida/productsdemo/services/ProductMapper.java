package com.frida.productsdemo.services;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

import com.frida.productsdemo.entity.ProductDto;
import com.frida.productsdemo.models.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product product);

    @InheritInverseConfiguration
    Product toEntity(ProductDto dto);
    
    List<ProductDto> toDtoList(List<Product> products);
    List<Product> toEntityList(List<ProductDto> dtos);
    
}