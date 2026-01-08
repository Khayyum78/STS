package com.example.inventory.infrastructure;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import com.example.inventory.domain.PagedResult;
import com.example.inventory.domain.Product;
import com.example.inventory.repository.ProductRepository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private final JpaProductRepository jpa;

    public ProductRepositoryImpl(JpaProductRepository jpa) { this.jpa = jpa; }

    private Product toDomain(JpaProductEntity e) {
        return new Product(e.getId(), e.getName(), e.getSku(), e.getQuantity(), e.getPrice());
    }
    private JpaProductEntity toEntity(Product p) {
        return JpaProductEntity.builder()
                .id(p.getId()).name(p.getName()).sku(p.getSku())
                .quantity(p.getQuantity()).price(p.getPrice())
                .build();
    }

    @Override public List<Product> findAll() {
        return jpa.findAll().stream().map(this::toDomain).toList();
    }
    @Override public Optional<Product> findById(Long id) {
        return jpa.findById(id).map(this::toDomain);
    }
    @Override public Product save(Product product) {
        return toDomain(jpa.save(toEntity(product)));
    }
    @Override public void deleteById(Long id) { jpa.deleteById(id); }
    @Override public boolean existsBySku(String sku) { return jpa.existsBySku(sku); }

    @Override
    public PagedResult<Product> findAllPaged(int page, int size, String sortBy, String direction, String q) {
        Sort sort = Sort.by((direction == null || direction.equalsIgnoreCase("asc")) ? Sort.Direction.ASC : Sort.Direction.DESC,
                            (sortBy == null || sortBy.isBlank()) ? "id" : sortBy);
        Pageable pageable = PageRequest.of(Math.max(page,0), Math.max(size,1), sort);

        Page<JpaProductEntity> pageData;
        if (q != null && !q.isBlank()) {
            pageData = jpa.findByNameContainingIgnoreCaseOrSkuContainingIgnoreCase(q, q, pageable);
        } else {
            pageData = jpa.findAll(pageable);
        }

        List<Product> items = pageData.getContent().stream().map(this::toDomain).toList();
        return new PagedResult<>(items, pageData.getTotalElements(), pageData.getTotalPages(), pageData.getNumber(), pageData.getSize());
    }
}
