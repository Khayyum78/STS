package com.example.inventory.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.inventory.domain.PagedResult;
import com.example.inventory.domain.Product;
import com.example.inventory.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository repo;
    public ProductService(ProductRepository repo) { this.repo = repo; }

    public List<Product> getAll() { return repo.findAll(); }

    public Product create(Product p) {
        if (repo.existsBySku(p.getSku())) throw new IllegalArgumentException("SKU already exists");
        return repo.save(p);
    }

    public Product update(Long id, Product p) {
        Product ex = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        ex.setName(p.getName()); ex.setSku(p.getSku());
        ex.setQuantity(p.getQuantity()); ex.setPrice(p.getPrice());
        return repo.save(ex);
    }

    public void delete(Long id) { repo.deleteById(id); }

    // NEW
    public PagedResult<Product> getPaged(int page, int size, String sortBy, String direction, String q) {
        return repo.findAllPaged(page, size, sortBy, direction, q);
    }
}
