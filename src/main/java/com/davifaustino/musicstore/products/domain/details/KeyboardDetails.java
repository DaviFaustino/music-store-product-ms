package com.davifaustino.musicstore.products.domain.details;

public record KeyboardDetails(
        String brand,
        Integer keys,
        Boolean weightedKeys,
        Boolean midiSupport,
        String powerSupply
) implements ProductDetails {

    @Override
    public ProductType type() {
        return ProductType.KEYBOARD;
    }
}
