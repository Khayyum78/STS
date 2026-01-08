package com.example.inventory.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales_orders")
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="order_no", nullable = false, unique = true, length = 32)
    private String orderNo;

    @Column(name="customer_name", length = 200)
    private String customerName;

    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false, length = 32)
    private SalesOrderStatus status = SalesOrderStatus.COMPLETED;

    @Column(name="currency", nullable = false, length = 8)
    private String currency = "TRY";

    @Column(name="subtotal", nullable = false)
    private Long subtotal; // kuruş cinsinden (TRY için)

    @Column(name="grand_total", nullable = false)
    private Long grandTotal; // kuruş cinsinden

    @Column(name="created_at", nullable = false)
    private Instant createdAt;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SalesOrderItem> items = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = Instant.now();
        if (status == null) status = SalesOrderStatus.COMPLETED;
        if (currency == null) currency = "TRY";
    }

    public void addItem(SalesOrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    // getters/setters
    public Long getId() { return id; }

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public SalesOrderStatus getStatus() { return status; }
    public void setStatus(SalesOrderStatus status) { this.status = status; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public Long getSubtotal() { return subtotal; }
    public void setSubtotal(Long subtotal) { this.subtotal = subtotal; }

    public Long getGrandTotal() { return grandTotal; }
    public void setGrandTotal(Long grandTotal) { this.grandTotal = grandTotal; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public List<SalesOrderItem> getItems() { return items; }
    public void setItems(List<SalesOrderItem> items) { this.items = items; }
}
