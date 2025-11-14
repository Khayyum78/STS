package com.example.inventory.controller.mapper;

import com.example.inventory.controller.dto.ProductRequest;
import com.example.inventory.controller.dto.ProductResponse;
import com.example.inventory.domain.Product;

public class ProductMapper {
    public static Product toDomain(ProductRequest r) {
        return new Product(null, r.getName(), r.getSku(), r.getQuantity(), r.getPrice());
    }
    public static ProductResponse toResponse(Product p) {
        return new ProductResponse(p.getId(), p.getName(), p.getSku(), p.getQuantity(), p.getPrice());
    }
}
