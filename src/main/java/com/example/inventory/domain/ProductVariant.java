package com.example.inventory.domain;

import java.math.BigDecimal;

public class ProductVariant {
    private Long id;
    private Long productId;
    private String sku;       // unique
    private String barcode;   // optional unique
    private String color;
    private String size;
    private BigDecimal price;
    private Integer quantity;

    public ProductVariant() {}

    public ProductVariant(Long id, Long productId, String sku, String barcode,
                          String color, String size, BigDecimal price, Integer quantity) {
        this.id = id; this.productId = productId; this.sku = sku; this.barcode = barcode;
        this.color = color; this.size = size; this.price = price; this.quantity = quantity;
    }

    public Long getId() { return id; }
    public Long getProductId() { return productId; }
    public String getSku() { return sku; }
    public String getBarcode() { return barcode; }
    public String getColor() { return color; }
    public String getSize() { return size; }
    public BigDecimal getPrice() { return price; }
    public Integer getQuantity() { return quantity; }

    public void setId(Long id) { this.id = id; }
    public void setProductId(Long productId) { this.productId = productId; }
    public void setSku(String sku) { this.sku = sku; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public void setColor(String color) { this.color = color; }
    public void setSize(String size) { this.size = size; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
