package com.example.inventory.application;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.inventory.application.dto.CreateStockMovementRequest;
import com.example.inventory.application.dto.StockRowResponse;
import com.example.inventory.domain.ProductVariant;
import com.example.inventory.domain.StockMovement;
import com.example.inventory.domain.StockType;
import com.example.inventory.repository.ProductVariantRepository;
import com.example.inventory.repository.StockMovementRepository;

@Service
public class StockService {

    private final StockMovementRepository repo;
    private final ProductVariantRepository variantRepo;

    public StockService(StockMovementRepository repo, ProductVariantRepository variantRepo) {
        this.repo = repo;
        this.variantRepo = variantRepo;
    }

    public List<StockMovement> getMovements(Long productId) {
        return repo.findByProductIdOrderByCreatedAtDesc(productId);
    }

    
    public List<StockRowResponse> getCurrentStockByVariant(Long productId) {
        var rows = repo.calcStockByVariant(productId, StockType.IN, StockType.OUT, StockType.ADJUST);

        // variantId listesi
        List<Long> variantIds = rows.stream()
                .map(StockMovementRepository.StockAggRow::getVariantId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        // id -> variant map
        Map<Long, ProductVariant> variantMap = variantIds.isEmpty()
        ? Map.of()
        : variantRepo.findByIdIn(variantIds).stream()
            .collect(Collectors.toMap(ProductVariant::getId, v -> v));
        return rows.stream()
                .map(r -> {
                    Long vid = r.getVariantId();
                    ProductVariant v = (vid == null) ? null : variantMap.get(vid);
                    String sku = (v == null) ? null : v.getSku();
                    String name = (v == null) ? null : v.getName();
                    return new StockRowResponse(vid, r.getQuantity(), sku, name);
                })
                .toList();
    }

    @Transactional
    public StockMovement createMovement(CreateStockMovementRequest req) {
        // OUT sırasında stok eksiye düşmesin
        if (req.getType() == StockType.OUT) {
            Integer current = repo.calcCurrentStock(
                    req.getProductId(),
                    req.getVariantId(),
                    StockType.IN,
                    StockType.OUT,
                    StockType.ADJUST
            );
            int after = (current == null ? 0 : current) - req.getQuantity();
            if (after < 0) {
                throw new IllegalArgumentException(
                        "Not enough stock. Current=" + (current == null ? 0 : current) + ", trying OUT=" + req.getQuantity()
                );
            }
        }

        StockMovement m = new StockMovement();
        m.setProductId(req.getProductId());
        m.setVariantId(req.getVariantId());
        m.setType(req.getType());
        m.setQuantity(req.getQuantity());
        m.setNote(req.getNote());

        return repo.save(m);
    }
}
