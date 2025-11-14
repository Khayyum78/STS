package com.example.inventory.infrastructure;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_variants",
       uniqueConstraints = {
           @UniqueConstraint(name="uk_variant_sku", columnNames = "sku"),
           @UniqueConstraint(name="uk_variant_barcode", columnNames = "barcode")
       })
@Getter @Setter @Builder
@NoArgsConstructor @AllArgsConstructor
public class JpaProductVariantEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="product_id", nullable=false)
    private Long productId; // Basit FK (explicit relation yerine ID tutuyoruz)

    @Column(nullable=false, length=120)
    private String sku;

    @Column(length=120)
    private String barcode;

    @Column(length=50)
    private String color;

    @Column(length=20)
    private String size;

    @Column(precision=14, scale=2, nullable=false)
    private BigDecimal price;

    @Column(nullable=false)
    private Integer quantity;
}
