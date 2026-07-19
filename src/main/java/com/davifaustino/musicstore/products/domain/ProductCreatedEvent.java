package com.davifaustino.musicstore.products.domain;

import java.math.BigDecimal;
import java.util.UUID;

import com.davifaustino.musicstore.products.domain.details.ProductType;

public record ProductCreatedEvent(
    UUID productId,
    String sku,
    String name,
    String description,
    BigDecimal price,
    ProductStatus status,
    ProductType type) {
}
