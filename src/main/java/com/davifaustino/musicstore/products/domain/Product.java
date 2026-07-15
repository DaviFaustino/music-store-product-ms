package com.davifaustino.musicstore.products.domain;

import com.davifaustino.musicstore.products.domain.details.ProductDetails;
import com.davifaustino.musicstore.products.domain.details.ProductType;
import com.davifaustino.musicstore.products.domain.valueobject.Money;
import com.davifaustino.musicstore.products.domain.valueobject.ProductId;
import com.davifaustino.musicstore.products.domain.valueobject.ProductName;
import com.davifaustino.musicstore.products.domain.valueobject.Sku;

import java.util.Objects;

public class Product {

    private final ProductId id;
    private final Sku sku;
    private ProductName name;
    private String description;
    private Money price;
    private ProductStatus status;
    private ProductDetails details;

    public Product(
            ProductId id,
            Sku sku,
            ProductName name,
            String description,
            Money price,
            ProductDetails details
    ) {
        this(id, sku, name, description, price, ProductStatus.ACTIVE, details);
    }

    public Product(
            ProductId id,
            Sku sku,
            ProductName name,
            String description,
            Money price,
            ProductStatus status,
            ProductDetails details
    ) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.sku = Objects.requireNonNull(sku, "sku must not be null");
        this.name = Objects.requireNonNull(name, "name must not be null");
        this.description = description;
        this.price = Objects.requireNonNull(price, "price must not be null");
        this.status = Objects.requireNonNull(status, "status must not be null");
        this.details = Objects.requireNonNull(details, "details must not be null");
    }

    public ProductId getId() {
        return id;
    }

    public Sku getSku() {
        return sku;
    }

    public ProductName getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Money getPrice() {
        return price;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public ProductDetails getDetails() {
        return details;
    }

    public ProductType getType() {
        return details.type();
    }
}
