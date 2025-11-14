package com.example.inventory.controller.dto;

import java.math.BigDecimal;
import java.util.List;

public class VariantBulkRequest {
    public Long productId;
    public List<String> colors;
    public List<String> sizes;
    public String baseSku;
    public BigDecimal price;
    public Integer quantity;
}
