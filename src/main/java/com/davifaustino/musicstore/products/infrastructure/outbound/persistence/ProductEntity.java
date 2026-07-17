package com.davifaustino.musicstore.products.infrastructure.outbound.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.davifaustino.musicstore.products.domain.ProductStatus;
import com.davifaustino.musicstore.products.domain.details.ProductDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Document(collection = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    private UUID id;
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private ProductStatus status;
    private ProductDetails details;
}
