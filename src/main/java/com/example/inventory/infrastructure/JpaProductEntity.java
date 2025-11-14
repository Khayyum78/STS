package com.example.inventory.infrastructure;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "products")
@Getter @Setter
@Builder
@NoArgsConstructor @AllArgsConstructor
public class JpaProductEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, length=200)
    private String name;

    @Column(nullable=false, unique=true, length=120)
    private String sku;

    @Column(nullable=false)
    private Integer quantity;

    @Column(nullable=false, precision=14, scale=2)
    private BigDecimal price;
}
