package com.davifaustino.musicstore.products.domain.details;

public sealed interface ProductDetails
        permits VinylDetails, CdDetails, CassetteDetails, InstrumentDetails, GuitarDetails,
                KeyboardDetails, DrumKitDetails, AccessoryDetails, AudioEquipmentDetails {

    ProductType type();
}
