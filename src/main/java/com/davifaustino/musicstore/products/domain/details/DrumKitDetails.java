package com.davifaustino.musicstore.products.domain.details;

public record DrumKitDetails(
        String brand,
        Integer pieces,
        String shellMaterial,
        Boolean includedHardware
) implements ProductDetails {

    @Override
    public ProductType type() {
        return ProductType.DRUM_KIT;
    }
}
