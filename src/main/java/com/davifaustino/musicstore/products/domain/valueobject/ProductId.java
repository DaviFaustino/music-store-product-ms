package com.davifaustino.musicstore.products.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record ProductId(UUID value) {

    public ProductId {
        Objects.requireNonNull(value, "product id must not be null");
    }

    public static ProductId newId() {
        return new ProductId(UUID.randomUUID());
    }

    public static ProductId fromString(String value) {
        return new ProductId(UUID.fromString(value));
    }

    public String asString() {
        return value.toString();
    }
}
