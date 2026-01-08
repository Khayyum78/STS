package com.example.inventory.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.inventory.application.ProductService;
import com.example.inventory.controller.dto.PagedResponse;
import com.example.inventory.controller.dto.ProductRequest;
import com.example.inventory.controller.dto.ProductResponse;
import com.example.inventory.controller.mapper.ProductMapper;
import com.example.inventory.domain.Product;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service) { this.service = service; }

    @GetMapping("/paged")
    public ResponseEntity<PagedResponse<ProductResponse>> listPaged(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sort,
        @RequestParam(defaultValue = "asc") String dir,
        @RequestParam(required = false) String q
    ) {
    var result = service.getPaged(page, size, sort, dir, q);
    var items = result.getItems().stream().map(ProductMapper::toResponse).toList();
    var body = new PagedResponse<>(items, result.getTotalElements(), result.getTotalPages(), result.getPage(), result.getSize());
    return ResponseEntity.ok(body);
    }

    
    @GetMapping
    public ResponseEntity<List<ProductResponse>> list() {
        var data = service.getAll().stream().map(ProductMapper::toResponse).toList();
        return ResponseEntity.ok(data);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest req) {
        Product created = service.create(ProductMapper.toDomain(req));
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductMapper.toResponse(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id, @Valid @RequestBody ProductRequest req) {
        Product updated = service.update(id, ProductMapper.toDomain(req));
        return ResponseEntity.ok(ProductMapper.toResponse(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
