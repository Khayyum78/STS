package com.example.inventory.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory.application.StockService;
import com.example.inventory.application.dto.CreateStockMovementRequest;
import com.example.inventory.application.dto.StockRowResponse;
import com.example.inventory.domain.StockMovement;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/stock")
public class StockController {

    private final StockService service;

    public StockController(StockService service) {
        this.service = service;
    }

    // GET /api/stock?productId=1
    @GetMapping
    public ResponseEntity<List<StockRowResponse>> getStock(@RequestParam Long productId) {
        return ResponseEntity.ok(service.getCurrentStockByVariant(productId));
    }

    // GET /api/stock/movements?productId=1
    @GetMapping("/movements")
    public ResponseEntity<List<StockMovement>> getMovements(@RequestParam Long productId) {
        return ResponseEntity.ok(service.getMovements(productId));
    }

    // POST /api/stock/movements
    @PostMapping("/movements")
    public ResponseEntity<StockMovement> createMovement(@Valid @RequestBody CreateStockMovementRequest req) {
        return ResponseEntity.ok(service.createMovement(req));
    }
}
