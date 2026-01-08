package com.example.inventory.application.dto;

import com.example.inventory.domain.StockType;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateStockMovementRequest {

    @NotNull
    private Long productId;

    private Long variantId;

    @NotNull
    private StockType type;

    @NotNull
    @Min(1)
    private Integer quantity;

    private String note;

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getVariantId() { return variantId; }
    public void setVariantId(Long variantId) { this.variantId = variantId; }

    public StockType getType() { return type; }
    public void setType(StockType type) { this.type = type; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}
