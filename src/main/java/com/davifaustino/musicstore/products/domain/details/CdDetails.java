package com.davifaustino.musicstore.products.domain.details;

public record CdDetails(
        String artist,
        String albumTitle,
        String label,
        Integer discCount,
        Integer releaseYear,
        String genre
) implements ProductDetails {

    @Override
    public ProductType type() {
        return ProductType.CD;
    }
}
