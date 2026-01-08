package com.example.inventory.repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.example.inventory.domain.PagedResult;
import com.example.inventory.domain.ProductVariant;

public interface ProductVariantRepository {
    ProductVariant save(ProductVariant v);
    void deleteById(Long id);
    Optional<ProductVariant> findById(Long id);
    boolean existsBySku(String sku);
    boolean existsByBarcode(String barcode);
    List<ProductVariant> findByProductId(Long productId);
    List<ProductVariant> findByIdIn(Collection<Long> ids);

    PagedResult<ProductVariant> findPaged(Integer page, Integer size, String sort, String dir,
                                          Long productId, String color, String sizeFilter, String q);

    // Bulk create için basit toplu kayıt
    List<ProductVariant> saveAll(List<ProductVariant> variants);
}
