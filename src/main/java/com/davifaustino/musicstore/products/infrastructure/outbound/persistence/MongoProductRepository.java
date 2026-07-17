package com.davifaustino.musicstore.products.infrastructure.outbound.persistence;

import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoProductRepository extends MongoRepository<ProductEntity, UUID> {

}
