package com.example.inventory.infrastructure;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<JpaProductEntity, Long> {
    boolean existsBySku(String sku);

    // basit arama: name veya sku içinde
    Page<JpaProductEntity> findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(String name, String sku, Pageable pageable);
}
