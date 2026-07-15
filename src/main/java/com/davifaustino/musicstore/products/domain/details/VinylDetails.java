package com.davifaustino.musicstore.products.domain.details;

import com.davifaustino.musicstore.products.domain.valueobject.DiscSize;
import com.davifaustino.musicstore.products.domain.valueobject.Rpm;

public record VinylDetails(
        String artist,
        String albumTitle,
        String label,
        Rpm rpm,
        DiscSize discSize,
        String pressing,
        Integer releaseYear,
        String genre
) implements ProductDetails {

    @Override
    public ProductType type() {
        return ProductType.VINYL;
    }
}
