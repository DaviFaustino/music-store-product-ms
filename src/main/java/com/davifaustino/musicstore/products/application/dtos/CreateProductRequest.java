package com.davifaustino.musicstore.products.application.dtos;

import com.davifaustino.musicstore.products.domain.details.AccessoryDetails;
import com.davifaustino.musicstore.products.domain.details.AudioEquipmentDetails;
import com.davifaustino.musicstore.products.domain.details.CassetteDetails;
import com.davifaustino.musicstore.products.domain.details.CdDetails;
import com.davifaustino.musicstore.products.domain.details.DrumKitDetails;
import com.davifaustino.musicstore.products.domain.details.GuitarDetails;
import com.davifaustino.musicstore.products.domain.details.InstrumentDetails;
import com.davifaustino.musicstore.products.domain.details.KeyboardDetails;
import com.davifaustino.musicstore.products.domain.details.ProductDetails;
import com.davifaustino.musicstore.products.domain.details.VinylDetails;
import com.davifaustino.musicstore.products.domain.details.ProductType;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;

public record CreateProductRequest(
        ProductType type,
        String name,
        String sku,
        String description,
        BigDecimal price,
        @JsonTypeInfo(
                use = JsonTypeInfo.Id.NAME,
                include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
                property = "type"
        )
        @JsonSubTypes({
                @JsonSubTypes.Type(value = VinylDetails.class, name = "VINYL"),
                @JsonSubTypes.Type(value = CdDetails.class, name = "CD"),
                @JsonSubTypes.Type(value = CassetteDetails.class, name = "CASSETTE"),
                @JsonSubTypes.Type(value = InstrumentDetails.class, name = "INSTRUMENT"),
                @JsonSubTypes.Type(value = GuitarDetails.class, name = "GUITAR"),
                @JsonSubTypes.Type(value = KeyboardDetails.class, name = "KEYBOARD"),
                @JsonSubTypes.Type(value = DrumKitDetails.class, name = "DRUM_KIT"),
                @JsonSubTypes.Type(value = AccessoryDetails.class, name = "ACCESSORY"),
                @JsonSubTypes.Type(value = AudioEquipmentDetails.class, name = "AUDIO_EQUIPMENT")
        })
        ProductDetails details
) {
}
