package com.davifaustino.musicstore.products.application.outbox;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.davifaustino.musicstore.products.infrastructure.outbound.persistence.outbox.OutboxRepository;


@Component
public class OutboxPublisher {

    private OutboxRepository outboxRepository;
    private RabbitTemplate rabbitTemplate;

    public OutboxPublisher(OutboxRepository outboxRepository, RabbitTemplate rabbitTemplate) {
        this.outboxRepository = outboxRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Scheduled(fixedDelay = 2000)
    public void publishPendingEvents() {
        var pendingEvents = outboxRepository.getByStatus(OutboxStatus.PENDING);

        for (var event : pendingEvents) {
            try {
                rabbitTemplate.convertAndSend(
                    "music-store.events",
                    event.getRoutingKey(),
                    new OutboxMessage(
                        event.getId(),
                        event.getCorrelationId(),
                        event.getEventType(),
                        event.getPayload()
                    )
                );
                event.processed();
            } catch (Exception e) {
                event.failedAttempt();
            }
            outboxRepository.save(event);
        }
    }
}
