package com.davifaustino.musicstore.products.infrastructure.outbound.persistence.outbox;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.davifaustino.musicstore.products.application.outbox.OutboxStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "outbox_events")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEventEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
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
}
