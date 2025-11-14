package com.example.inventory.controller;

import com.example.inventory.controller.dto.*;
import com.example.inventory.controller.mapper.VariantMapper;
import com.example.inventory.domain.PagedResult;
import com.example.inventory.domain.ProductVariant;
import com.example.inventory.application.ProductVariantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/variants")
public class ProductVariantController {

    private final ProductVariantService service;

    public ProductVariantController(ProductVariantService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<PagedResponse<VariantResponse>> listPaged(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String dir,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) String color,
            @RequestParam(value = "sizeFilter", required = false) String sizeFilter,
            @RequestParam(required = false) String q
    ){
        PagedResult<ProductVariant> pr = service.getPaged(page, size, sort, dir, productId, color, sizeFilter, q);
        var items = pr.getItems().stream().map(VariantMapper::toResponse).collect(Collectors.toList());
        var body = new PagedResponse<>(items, pr.getTotalElements(), pr.getTotalPages(), pr.getPage(), pr.getSize());
        return ResponseEntity.ok(body);
    }

    @GetMapping("/by-product/{productId}")
    public ResponseEntity<?> listByProduct(@PathVariable Long productId){
        var items = service.listByProduct(productId).stream().map(VariantMapper::toResponse).toList();
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<VariantResponse> create(@RequestBody VariantRequest req){
        var saved = service.create(VariantMapper.toDomain(req));
        return ResponseEntity.ok(VariantMapper.toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VariantResponse> update(@PathVariable Long id, @RequestBody VariantRequest req){
        var d = VariantMapper.toDomain(req); d.setId(id);
        var saved = service.update(id, d);
        return ResponseEntity.ok(VariantMapper.toResponse(saved));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> bulk(@RequestBody VariantBulkRequest req){
        var out = service.bulkCreate(req.productId, req.colors, req.sizes, req.baseSku, req.price, req.quantity)
                .stream().map(VariantMapper::toResponse).toList();
        return ResponseEntity.ok(out);
    }
}
