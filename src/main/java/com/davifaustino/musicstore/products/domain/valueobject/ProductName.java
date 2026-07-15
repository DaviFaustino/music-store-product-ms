package com.davifaustino.musicstore.products.domain.valueobject;

public record ProductName(String value) {

    public ProductName {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("product name must not be blank");
        }

        value = value.trim();
    }
}
