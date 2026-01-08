package com.example.inventory.application.dto;

public class StockRowResponse {
    private Long variantId;
    private Integer quantity;

    private String variantSku;
    private String variantName;

    public StockRowResponse(Long variantId, Integer quantity, String variantSku, String variantName) {
        this.variantId = variantId;
        this.quantity = quantity;
        this.variantSku = variantSku;
        this.variantName = variantName;
    }

    public Long getVariantId() { return variantId; }
    public Integer getQuantity() { return quantity; }

    public String getVariantSku() { return variantSku; }
    public String getVariantName() { return variantName; }
}
