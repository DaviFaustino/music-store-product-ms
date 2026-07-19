package com.davifaustino.musicstore.products.infrastructure.outbound.persistence.outbox;

import java.util.List;

import org.springframework.stereotype.Component;

import com.davifaustino.musicstore.products.application.outbox.OutboxEvent;
import com.davifaustino.musicstore.products.application.outbox.OutboxStatus;

@Component
public class OutboxRepository {

    private final MongoOutboxRepository mongoOutboxRepository;

    public OutboxRepository(MongoOutboxRepository mongoOutboxRepository) {
        this.mongoOutboxRepository = mongoOutboxRepository;
    }

    public OutboxEvent save(OutboxEvent event) {
        var outboxEvent = OutboxPersistenceMapper.toEntity(event);
        var savedOutboxEvent = mongoOutboxRepository.save(outboxEvent);
        return OutboxPersistenceMapper.toDomain(savedOutboxEvent);
    }

    public List<OutboxEvent> getByStatus(OutboxStatus eventStatus) {
        return mongoOutboxRepository.findByStatus(eventStatus).stream()
                .map(OutboxPersistenceMapper::toDomain)
                .toList();
    }
}
