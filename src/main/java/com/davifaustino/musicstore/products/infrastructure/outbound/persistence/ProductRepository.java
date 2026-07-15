package com.davifaustino.musicstore.products.infrastructure.outbound.persistence;

import com.davifaustino.musicstore.products.domain.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductRepository {

    private final JpaProductRepository jpaProductRepository;
    private final ProductPersistenceMapper productPersistenceMapper;

    public ProductRepository(
            JpaProductRepository jpaProductRepository,
            ProductPersistenceMapper productPersistenceMapper
    ) {
        this.jpaProductRepository = jpaProductRepository;
        this.productPersistenceMapper = productPersistenceMapper;
    }
    
    public Product save(Product product) {
        ProductEntity productEntity = productPersistenceMapper.toEntity(product);
        ProductEntity savedProduct = jpaProductRepository.save(productEntity);

        return productPersistenceMapper.toDomain(savedProduct);
    }
}
