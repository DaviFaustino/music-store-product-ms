package com.davifaustino.musicstore.products.application.outbox;

import java.time.Instant;
import java.util.UUID;

public class OutboxEvent {

    private UUID id;
    private UUID correlationId;
    private String aggregateType;
    private UUID aggregateId;
    private String eventType;
    private String routingKey;
    private String payload;
    private OutboxStatus status;
    private Integer attempts;
    private Instant occurredAt;
    private Instant publishedAt;

    private static final int MAX_ATTEMPTS = 10;

    public OutboxEvent() {
    }

    public OutboxEvent(UUID id, UUID correlationId, String aggregateType, UUID aggregateId, String eventType, String routingKey, String payload, OutboxStatus status,
            Integer attempts, Instant occurredAt, Instant publishedAt) {
        this.id = id;
        this.correlationId = correlationId;
        this.aggregateType = aggregateType;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.routingKey = routingKey;
        this.payload = payload;
        this.status = status;
        this.attempts = attempts;
        this.occurredAt = occurredAt;
        this.publishedAt = publishedAt;
    }

    public static OutboxEvent pending(UUID correlationId, String aggregateType, UUID aggregateId, String eventType, String routingKey, String data) {
        return new OutboxEvent(
            UUID.randomUUID(),
            correlationId,
            aggregateType,
            aggregateId,
            eventType,
            routingKey,
            data,
            OutboxStatus.PENDING,
            0,
            Instant.now(),
            null
        );
    }

    public void failedAttempt() {
        attempts++;

        if (attempts >= MAX_ATTEMPTS) {
            status = OutboxStatus.FAILED;
        }
    }

    public void processed() {
        status = OutboxStatus.PROCESSED;
        publishedAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(UUID correlationId) {
        this.correlationId = correlationId;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public void setAggregateType(String aggregateType) {
        this.aggregateType = aggregateType;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public OutboxStatus getStatus() {
        return status;
    }

    public void setStatus(OutboxStatus status) {
        this.status = status;
    }

    public Integer getAttempts() {
        return attempts;
    }

    public void setAttempts(Integer attempts) {
        this.attempts = attempts;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    public void setOccurredAt(Instant occurredAt) {
        this.occurredAt = occurredAt;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }
}
