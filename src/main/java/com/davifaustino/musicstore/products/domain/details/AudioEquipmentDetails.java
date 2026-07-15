package com.davifaustino.musicstore.products.domain.details;

import java.util.List;
import java.util.Objects;

public record AudioEquipmentDetails(
        String brand,
        String model,
        String equipmentType,
        String power,
        List<String> connectivity
) implements ProductDetails {

    public AudioEquipmentDetails {
        connectivity = List.copyOf(Objects.requireNonNullElse(connectivity, List.of()));
    }

    @Override
    public ProductType type() {
        return ProductType.AUDIO_EQUIPMENT;
    }
}
