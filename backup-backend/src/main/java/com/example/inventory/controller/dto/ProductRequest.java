package com.example.inventory.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductRequest {
    @NotBlank private String name;
    @NotBlank private String sku;
    @PositiveOrZero private Integer quantity;
    @PositiveOrZero private Double price;

    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getSku() { return sku; } public void setSku(String sku) { this.sku = sku; }
    public Integer getQuantity() { return quantity; } public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Double getPrice() { return price; } public void setPrice(Double price) { this.price = price; }
}
