package com.example.inventory.controller.dto;

import java.math.BigDecimal;

public class VariantRequest {
    public Long productId;
    public String sku;
    public String barcode;
    public String color;
    public String size;
    public BigDecimal price;
    public Integer quantity;
}
