package com.example.inventory.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.example.inventory.domain.PagedResult;
import com.example.inventory.domain.ProductVariant;
import com.example.inventory.repository.ProductVariantRepository;

@Repository
public class ProductVariantRepositoryImpl implements ProductVariantRepository {

    private final JpaProductVariantRepository jpa;

    public ProductVariantRepositoryImpl(JpaProductVariantRepository jpa) {
        this.jpa = jpa;
    }

    private ProductVariant toDomain(JpaProductVariantEntity e){
        return new ProductVariant(
                e.getId(), e.getProductId(), e.getSku(), e.getBarcode(),
                e.getColor(), e.getSize(), e.getPrice(), e.getQuantity()
        );
    }
    private JpaProductVariantEntity toEntity(ProductVariant d){
        return JpaProductVariantEntity.builder()
                .id(d.getId())
                .productId(d.getProductId())
                .sku(d.getSku())
                .barcode(d.getBarcode())
                .color(d.getColor())
                .size(d.getSize())
                .price(d.getPrice())
                .quantity(d.getQuantity())
                .build();
    }

    @Override public ProductVariant save(ProductVariant v) { return toDomain(jpa.save(toEntity(v))); }
    @Override public void deleteById(Long id) { jpa.deleteById(id); }
    @Override public Optional<ProductVariant> findById(Long id) { return jpa.findById(id).map(this::toDomain); }
    @Override public boolean existsBySku(String sku) { return jpa.existsBySku(sku); }
    @Override public boolean existsByBarcode(String barcode) { return barcode != null && !barcode.isBlank() && jpa.existsByBarcode(barcode); }
    @Override public List<ProductVariant> findByProductId(Long productId) { return jpa.findByProductId(productId).stream().map(this::toDomain).toList(); }

    @Override
    public PagedResult<ProductVariant> findPaged(Integer page, Integer size, String sort, String dir,
                                                 Long productId, String color, String sizeFilter, String q) {
        Sort s = Sort.by((dir==null || dir.equalsIgnoreCase("asc"))? Sort.Direction.ASC:Sort.Direction.DESC,
                (sort==null || sort.isBlank())? "id" : sort);
        Pageable pageable = PageRequest.of(Math.max(0,page==null?0:page), Math.max(1,size==null?10:size), s);

        Page<JpaProductVariantEntity> data;
        if (productId != null) {
            data = jpa.findByProductIdAndColorContainingIgnoreCaseAndSizeContainingIgnoreCase(
                    productId, color==null?"":color, sizeFilter==null?"":sizeFilter, pageable
            );
        } else if (q != null && !q.isBlank()) {
            data = jpa.findBySkuContainingIgnoreCaseOrBarcodeContainingIgnoreCaseOrColorContainingIgnoreCase(q, q, q, pageable);
        } else {
            data = jpa.findAll(pageable);
        }

        var items = data.getContent().stream().map(this::toDomain).toList();
        return new PagedResult<>(items, data.getTotalElements(), data.getTotalPages(), data.getNumber(), data.getSize());
    }

    @Override
    public List<ProductVariant> saveAll(List<ProductVariant> variants) {
        var saved = jpa.saveAll(variants.stream().map(this::toEntity).toList());
        return saved.stream().map(this::toDomain).toList();
    }
}
