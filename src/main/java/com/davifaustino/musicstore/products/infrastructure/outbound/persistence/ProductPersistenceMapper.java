package com.davifaustino.musicstore.products.infrastructure.outbound.persistence;

import com.davifaustino.musicstore.products.domain.Product;
import com.davifaustino.musicstore.products.domain.valueobject.Money;
import com.davifaustino.musicstore.products.domain.valueobject.ProductId;
import com.davifaustino.musicstore.products.domain.valueobject.ProductName;
import com.davifaustino.musicstore.products.domain.valueobject.Sku;
import org.springframework.stereotype.Component;

@Component
public class ProductPersistenceMapper {

    public ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.getId().value(),
                product.getSku().value(),
                product.getName().value(),
                product.getDescription(),
                product.getPrice().amount(),
                product.getStatus(),
                product.getDetails()
        );
    }

    public Product toDomain(ProductEntity productEntity) {
        return new Product(
                new ProductId(productEntity.getId()),
                new Sku(productEntity.getSku()),
                new ProductName(productEntity.getName()),
                productEntity.getDescription(),
                new Money(productEntity.getPrice()),
                productEntity.getStatus(),
                productEntity.getDetails()
        );
    }
}
