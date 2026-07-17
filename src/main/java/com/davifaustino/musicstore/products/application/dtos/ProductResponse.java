package com.davifaustino.musicstore.products.application.dtos;

import com.davifaustino.musicstore.products.domain.ProductStatus;
import com.davifaustino.musicstore.products.domain.details.ProductDetails;
import com.davifaustino.musicstore.products.domain.details.ProductType;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String sku,
        String name,
        String description,
        BigDecimal price,
        ProductStatus status,
        ProductType type,
        ProductDetails details) {
}
