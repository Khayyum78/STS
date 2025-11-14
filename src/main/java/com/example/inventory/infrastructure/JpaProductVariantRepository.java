package com.example.inventory.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaProductVariantRepository extends JpaRepository<JpaProductVariantEntity, Long> {
    boolean existsBySku(String sku);
    boolean existsByBarcode(String barcode);
    List<JpaProductVariantEntity> findByProductId(Long productId);

    Page<JpaProductVariantEntity> findByProductIdAndColorContainingIgnoreCaseAndSizeContainingIgnoreCase(
            Long productId, String color, String size, Pageable pageable
    );

    Page<JpaProductVariantEntity> findBySkuContainingIgnoreCaseOrBarcodeContainingIgnoreCaseOrColorContainingIgnoreCase(
            String sku, String barcode, String color, Pageable pageable
    );
}
