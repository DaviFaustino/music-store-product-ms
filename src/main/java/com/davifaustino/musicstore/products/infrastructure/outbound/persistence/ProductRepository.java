package com.davifaustino.musicstore.products.infrastructure.outbound.persistence;

import com.davifaustino.musicstore.products.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductRepository {

    private final MongoProductRepository mongoProductRepository;
    private final ProductPersistenceMapper productPersistenceMapper;

    public ProductRepository(
            MongoProductRepository mongoProductRepository,
            ProductPersistenceMapper productPersistenceMapper
    ) {
        this.mongoProductRepository = mongoProductRepository;
        this.productPersistenceMapper = productPersistenceMapper;
    }
    
    public Product save(Product product) {
        ProductEntity productEntity = productPersistenceMapper.toEntity(product);
        ProductEntity savedProduct = mongoProductRepository.save(productEntity);

        return productPersistenceMapper.toDomain(savedProduct);
    }
}
