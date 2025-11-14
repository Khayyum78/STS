package com.example.inventory.repository;

import java.util.List;
import java.util.Optional;

import com.example.inventory.domain.PagedResult;
import com.example.inventory.domain.Product;

public interface ProductRepository {
    List<Product> findAll();
    Optional<Product> findById(Long id);
    Product save(Product product);
    void deleteById(Long id);
    boolean existsBySku(String sku);

    // NEW: paging + sorting + basit arama (q = name/sku contains)
    PagedResult<Product> findAllPaged(int page, int size, String sortBy, String direction, String q);
}
