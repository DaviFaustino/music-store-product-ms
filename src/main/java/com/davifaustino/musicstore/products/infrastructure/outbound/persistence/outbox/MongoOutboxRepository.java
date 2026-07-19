package com.davifaustino.musicstore.products.infrastructure.outbound.persistence.outbox;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.davifaustino.musicstore.products.application.outbox.OutboxStatus;

public interface MongoOutboxRepository extends MongoRepository<OutboxEventEntity, UUID> {

    List<OutboxEventEntity> findByStatus(OutboxStatus status);
}
