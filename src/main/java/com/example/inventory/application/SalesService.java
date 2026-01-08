package com.example.inventory.application;

import com.example.inventory.application.dto.CreateSalesOrderRequest;
import com.example.inventory.application.dto.SalesOrderResponse;
import com.example.inventory.domain.SalesOrder;
import com.example.inventory.domain.SalesOrderItem;
import com.example.inventory.domain.StockType;
import com.example.inventory.application.dto.CreateStockMovementRequest;
import com.example.inventory.repository.SalesOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class SalesService {

    private final SalesOrderRepository salesOrderRepository;
    private final StockService stockService;

    public SalesService(SalesOrderRepository salesOrderRepository, StockService stockService) {
        this.salesOrderRepository = salesOrderRepository;
        this.stockService = stockService;
    }

    @Transactional
    public SalesOrderResponse create(CreateSalesOrderRequest req) {
        SalesOrder order = new SalesOrder();
        order.setOrderNo(generateOrderNo());
        order.setCustomerName(req.getCustomerName());
        order.setCurrency(req.getCurrency() == null ? "TRY" : req.getCurrency());

        long subtotal = 0;

        for (CreateSalesOrderRequest.Item it : req.getItems()) {
            long lineTotal = (long) it.getQuantity() * it.getUnitPrice();
            subtotal += lineTotal;

            SalesOrderItem item = new SalesOrderItem();
            item.setProductId(it.getProductId());
            item.setVariantId(it.getVariantId());
            item.setQuantity(it.getQuantity());
            item.setUnitPrice(it.getUnitPrice());
            item.setLineTotal(lineTotal);

            order.addItem(item);
        }

        order.setSubtotal(subtotal);
        order.setGrandTotal(subtotal);

        // 1) önce order kaydet (id oluşsun)
        SalesOrder saved = salesOrderRepository.save(order);

        // 2) stok düş (OUT). StockService stok yetmiyorsa IllegalArgumentException atar -> 400
        for (SalesOrderItem it : saved.getItems()) {
            CreateStockMovementRequest sm = new CreateStockMovementRequest();
            sm.setProductId(it.getProductId());
            sm.setVariantId(it.getVariantId());
            sm.setType(StockType.OUT);
            sm.setQuantity(it.getQuantity());
            sm.setNote("SALE " + saved.getOrderNo());
            stockService.createMovement(sm);
        }

        return toResponse(saved);
    }

    public SalesOrderResponse getById(Long id) {
        SalesOrder order = salesOrderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sales order not found: " + id));
        return toResponse(order);
    }

    private String generateOrderNo() {
        // basit: SO-XXXXXXXX
        return "SO-" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }

    private SalesOrderResponse toResponse(SalesOrder o) {
        SalesOrderResponse r = new SalesOrderResponse();
        r.id = o.getId();
        r.orderNo = o.getOrderNo();
        r.customerName = o.getCustomerName();
        r.status = String.valueOf(o.getStatus());
        r.currency = o.getCurrency();
        r.subtotal = o.getSubtotal();
        r.grandTotal = o.getGrandTotal();
        r.createdAt = o.getCreatedAt();
        r.items = o.getItems().stream().map(i -> {
            SalesOrderResponse.Item ri = new SalesOrderResponse.Item();
            ri.id = i.getId();
            ri.productId = i.getProductId();
            ri.variantId = i.getVariantId();
            ri.quantity = i.getQuantity();
            ri.unitPrice = i.getUnitPrice();
            ri.lineTotal = i.getLineTotal();
            return ri;
        }).toList();
        return r;
    }
}
