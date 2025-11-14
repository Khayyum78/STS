package com.example.inventory.controller.mapper;

import com.example.inventory.controller.dto.VariantRequest;
import com.example.inventory.controller.dto.VariantResponse;
import com.example.inventory.domain.ProductVariant;

public class VariantMapper {
    public static ProductVariant toDomain(VariantRequest r){
        return new ProductVariant(null, r.productId, r.sku, r.barcode, r.color, r.size, r.price, r.quantity);
    }
    public static VariantResponse toResponse(ProductVariant v){
        VariantResponse resp = new VariantResponse();
        resp.id = v.getId(); resp.productId = v.getProductId();
        resp.sku = v.getSku(); resp.barcode = v.getBarcode();
        resp.color = v.getColor(); resp.size = v.getSize();
        resp.price = v.getPrice(); resp.quantity = v.getQuantity();
        return resp;
    }
}
