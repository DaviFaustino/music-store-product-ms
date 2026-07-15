package com.davifaustino.musicstore.products.infrastructure.outbound.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface JpaProductRepository extends MongoRepository<ProductEntity, String> {

}
