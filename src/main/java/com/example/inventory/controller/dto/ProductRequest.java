package com.example.inventory.controller.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductRequest {
    @NotBlank private String name;
    @NotBlank private String sku;
    @PositiveOrZero private Integer quantity;
    @PositiveOrZero private BigDecimal price;

    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getSku() { return sku; } public void setSku(String sku) { this.sku = sku; }
    public Integer getQuantity() { return quantity; } public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public BigDecimal getPrice() { return price; } public void setPrice(BigDecimal price) { this.price = price; }
}
