package com.example.inventory.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "sales_order_items")
public class SalesOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="order_id", nullable = false)
    private SalesOrder order;

    @Column(name="product_id", nullable = false)
    private Long productId;

    @Column(name="variant_id")
    private Long variantId;

    @Column(name="quantity", nullable = false)
    private Integer quantity;

    @Column(name="unit_price", nullable = false)
    private Long unitPrice; // kuruş

    @Column(name="line_total", nullable = false)
    private Long lineTotal; // kuruş

    // getters/setters
    public Long getId() { return id; }

    public SalesOrder getOrder() { return order; }
    public void setOrder(SalesOrder order) { this.order = order; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public Long getVariantId() { return variantId; }
    public void setVariantId(Long variantId) { this.variantId = variantId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Long getUnitPrice() { return unitPrice; }
    public void setUnitPrice(Long unitPrice) { this.unitPrice = unitPrice; }

    public Long getLineTotal() { return lineTotal; }
    public void setLineTotal(Long lineTotal) { this.lineTotal = lineTotal; }
}
