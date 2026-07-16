package com.davifaustino.musicstore.products.application;

import com.davifaustino.musicstore.products.application.dtos.CreateProductRequest;
import com.davifaustino.musicstore.products.application.dtos.ProductResponse;
import com.davifaustino.musicstore.products.domain.Product;
import com.davifaustino.musicstore.products.domain.details.AccessoryDetails;
import com.davifaustino.musicstore.products.domain.details.AudioEquipmentDetails;
import com.davifaustino.musicstore.products.domain.details.CassetteDetails;
import com.davifaustino.musicstore.products.domain.details.CdDetails;
import com.davifaustino.musicstore.products.domain.details.DrumKitDetails;
import com.davifaustino.musicstore.products.domain.details.GuitarDetails;
import com.davifaustino.musicstore.products.domain.details.InstrumentDetails;
import com.davifaustino.musicstore.products.domain.details.KeyboardDetails;
import com.davifaustino.musicstore.products.domain.details.ProductDetails;
import com.davifaustino.musicstore.products.domain.details.ProductType;
import com.davifaustino.musicstore.products.domain.details.VinylDetails;
import com.davifaustino.musicstore.products.domain.valueobject.Money;
import com.davifaustino.musicstore.products.domain.valueobject.ProductId;
import com.davifaustino.musicstore.products.domain.valueobject.ProductName;
import com.davifaustino.musicstore.products.domain.valueobject.Sku;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProductMapper {

    public Product toDomain(CreateProductRequest productRequest) {
        return new Product(
                new ProductId(UUID.randomUUID().toString()),
                new Sku(productRequest.sku()),
                new ProductName(productRequest.name()),
                productRequest.description(),
                new Money(productRequest.price()),
                toDetails(productRequest.type(), productRequest.details())
        );
    }

    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId().value(),
                product.getSku().value(),
                product.getName().value(),
                product.getDescription(),
                product.getPrice().amount(),
                product.getStatus(),
                product.getType(),
                product.getDetails()
        );
    }

    private ProductDetails toDetails(ProductType type, ProductDetails detailsRequest) {
        if (type == null) {
            throw new IllegalArgumentException("type must not be null");
        }

        if (detailsRequest == null) {
            throw new IllegalArgumentException("details must not be null");
        }

        if (type != detailsRequest.type()) {
            throw new IllegalArgumentException("product type must match details type");
        }

        return switch (detailsRequest) {
            case VinylDetails request -> new VinylDetails(
                    request.artist(),
                    request.albumTitle(),
                    request.label(),
                    request.rpm(),
                    request.discSize(),
                    request.pressing(),
                    request.releaseYear(),
                    request.genre()
            );
            case CdDetails request -> new CdDetails(
                    request.artist(),
                    request.albumTitle(),
                    request.label(),
                    request.discCount(),
                    request.releaseYear(),
                    request.genre()
            );
            case CassetteDetails request -> new CassetteDetails(
                    request.artist(),
                    request.albumTitle(),
                    request.label(),
                    request.condition(),
                    request.releaseYear()
            );
            case InstrumentDetails request -> new InstrumentDetails(
                    request.brand(),
                    request.model(),
                    request.instrumentType(),
                    request.condition(),
                    request.handedness(),
                    request.color(),
                    request.serialNumber()
            );
            case GuitarDetails request -> new GuitarDetails(
                    request.brand(),
                    request.model(),
                    request.guitarType(),
                    request.pickups(),
                    request.strings(),
                    request.handedness()
            );
            case KeyboardDetails request -> new KeyboardDetails(
                    request.brand(),
                    request.keys(),
                    request.weightedKeys(),
                    request.midiSupport(),
                    request.powerSupply()
            );
            case DrumKitDetails request -> new DrumKitDetails(
                    request.brand(),
                    request.pieces(),
                    request.shellMaterial(),
                    request.includedHardware()
            );
            case AccessoryDetails request -> new AccessoryDetails(
                    request.accessoryType(),
                    request.compatibleWith(),
                    request.brand(),
                    request.size()
            );
            case AudioEquipmentDetails request -> new AudioEquipmentDetails(
                    request.brand(),
                    request.model(),
                    request.equipmentType(),
                    request.power(),
                    request.connectivity()
            );
        };
    }
}
