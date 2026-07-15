package com.davifaustino.musicstore.products.domain.valueobject;

public record ProductId(String value) {

    public ProductId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("product id must not be blank");
        }

        value = value.trim();
    }
}
