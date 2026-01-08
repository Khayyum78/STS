package com.example.inventory.application.dto;

import java.time.Instant;
import java.util.List;

public class SalesOrderResponse {
    public Long id;
    public String orderNo;
    public String customerName;
    public String status;
    public String currency;
    public Long subtotal;
    public Long grandTotal;
    public Instant createdAt;
    public List<Item> items;

    public static class Item {
        public Long id;
        public Long productId;
        public Long variantId;
        public Integer quantity;
        public Long unitPrice;
        public Long lineTotal;
    }
}
