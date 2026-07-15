package com.davifaustino.musicstore.products.domain.valueobject;

public record Sku(String value) {

    public Sku {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("sku must not be blank");
        }

        value = value.trim().toUpperCase();
    }
}
