package com.davifaustino.musicstore.products.domain.details;

import com.davifaustino.musicstore.products.domain.valueobject.Handedness;

public record GuitarDetails(
        String brand,
        String model,
        String guitarType,
        String pickups,
        Integer strings,
        Handedness handedness
) implements ProductDetails {

    @Override
    public ProductType type() {
        return ProductType.GUITAR;
    }
}
