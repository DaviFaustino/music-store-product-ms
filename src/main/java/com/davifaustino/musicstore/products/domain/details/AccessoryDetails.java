package com.davifaustino.musicstore.products.domain.details;

import java.util.List;
import java.util.Objects;

public record AccessoryDetails(
        String accessoryType,
        List<String> compatibleWith,
        String brand,
        String size
) implements ProductDetails {

    public AccessoryDetails {
        compatibleWith = List.copyOf(Objects.requireNonNullElse(compatibleWith, List.of()));
    }

    @Override
    public ProductType type() {
        return ProductType.ACCESSORY;
    }
}
