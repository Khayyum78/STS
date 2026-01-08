package com.example.inventory.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.inventory.domain.StockMovement;
import com.example.inventory.domain.StockType;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findByProductIdOrderByCreatedAtDesc(Long productId);

    @Query("""
        select coalesce(sum(
            case
              when m.type = :inType then m.quantity
              when m.type = :outType then -m.quantity
              when m.type = :adjType then m.quantity
              else 0
            end
        ), 0)
        from StockMovement m
        where m.productId = :productId
          and ((:variantId is null and m.variantId is null) or (m.variantId = :variantId))
    """)
    Integer calcCurrentStock(Long productId, Long variantId, StockType inType, StockType outType, StockType adjType);

    @Query("""
        select m.variantId as variantId,
               coalesce(sum(
                 case
                   when m.type = :inType then m.quantity
                   when m.type = :outType then -m.quantity
                   when m.type = :adjType then m.quantity
                   else 0
                 end
               ), 0) as quantity
        from StockMovement m
        where m.productId = :productId
        group by m.variantId
    """)
    List<StockAggRow> calcStockByVariant(Long productId, StockType inType, StockType outType, StockType adjType);

    interface StockAggRow {
        Long getVariantId();
        Integer getQuantity();
    }
}
