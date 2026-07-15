package com.davifaustino.musicstore.products.domain.details;

import com.davifaustino.musicstore.products.domain.valueobject.Condition;
import com.davifaustino.musicstore.products.domain.valueobject.Handedness;

public record InstrumentDetails(
        String brand,
        String model,
        String instrumentType,
        Condition condition,
        Handedness handedness,
        String color,
        String serialNumber
) implements ProductDetails {

    @Override
    public ProductType type() {
        return ProductType.INSTRUMENT;
    }
}
