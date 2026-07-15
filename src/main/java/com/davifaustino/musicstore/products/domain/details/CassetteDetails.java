package com.davifaustino.musicstore.products.domain.details;

import com.davifaustino.musicstore.products.domain.valueobject.Condition;

public record CassetteDetails(
        String artist,
        String albumTitle,
        String label,
        Condition condition,
        Integer releaseYear
) implements ProductDetails {

    @Override
    public ProductType type() {
        return ProductType.CASSETTE;
    }
}
