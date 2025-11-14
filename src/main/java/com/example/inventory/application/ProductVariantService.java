package com.example.inventory.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.inventory.domain.PagedResult;
import com.example.inventory.domain.ProductVariant;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.ProductVariantRepository;

@Service
public class ProductVariantService {
    private final ProductVariantRepository variantRepo;
    private final ProductRepository productRepo;

    public ProductVariantService(ProductVariantRepository variantRepo, ProductRepository productRepo) {
        this.variantRepo = variantRepo;
        this.productRepo = productRepo;
    }

    public ProductVariant create(ProductVariant v) {
        // basit validasyonlar
        if (variantRepo.existsBySku(v.getSku())) throw new IllegalArgumentException("SKU already exists");
        if (v.getBarcode()!=null && !v.getBarcode().isBlank() && variantRepo.existsByBarcode(v.getBarcode()))
            throw new IllegalArgumentException("Barcode already exists");
        productRepo.findById(v.getProductId()).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return variantRepo.save(v);
    }

    public ProductVariant update(Long id, ProductVariant v) {
        var ex = variantRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Variant not found"));
        // SKU değişiyorsa çakışma kontrolü
        if (!ex.getSku().equals(v.getSku()) && variantRepo.existsBySku(v.getSku()))
            throw new IllegalArgumentException("SKU already exists");
        ex.setSku(v.getSku()); ex.setBarcode(v.getBarcode());
        ex.setColor(v.getColor()); ex.setSize(v.getSize());
        ex.setPrice(v.getPrice()); ex.setQuantity(v.getQuantity());
        return variantRepo.save(ex);
    }

    public void delete(Long id) { variantRepo.deleteById(id); }

    public PagedResult<ProductVariant> getPaged(Integer page, Integer size, String sort, String dir,
                                                Long productId, String color, String sizeFilter, String q) {
        return variantRepo.findPaged(page, size, sort, dir, productId, color, sizeFilter, q);
    }

    public List<ProductVariant> listByProduct(Long productId) {
        return variantRepo.findByProductId(productId);
    }

    // Basit bulk: colors x sizes matrisi + baseSku + price + qty
    public List<ProductVariant> bulkCreate(Long productId, List<String> colors, List<String> sizes,
                                           String baseSku, BigDecimal price, Integer qty) {
        productRepo.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        List<ProductVariant> toSave = new ArrayList<>();
        for (String c: colors) {
            for (String s: sizes) {
                String code = baseSku + "-" + normalize(c) + "-" + s.toUpperCase();
                if (variantRepo.existsBySku(code)) continue; // atla (idempotent)
                toSave.add(new ProductVariant(null, productId, code, null, c, s, price, qty));
            }
        }
        if (toSave.isEmpty()) return List.of();
        return variantRepo.saveAll(toSave);
    }

    private String normalize(String color){
        return color.trim().replace(' ', '-').toUpperCase();
    }
}
