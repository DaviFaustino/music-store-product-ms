package com.davifaustino.musicstore.products.application;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.davifaustino.musicstore.products.application.dtos.CreateProductRequest;
import com.davifaustino.musicstore.products.application.dtos.ProductResponse;
import com.davifaustino.musicstore.products.application.outbox.OutboxEvent;
import com.davifaustino.musicstore.products.domain.Product;
import com.davifaustino.musicstore.products.domain.ProductCreatedEvent;
import com.davifaustino.musicstore.products.infrastructure.outbound.persistence.ProductRepository;
import com.davifaustino.musicstore.products.infrastructure.outbound.persistence.outbox.OutboxRepository;

import tools.jackson.databind.ObjectMapper;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, ObjectMapper objectMapper, OutboxRepository outboxRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.objectMapper = objectMapper;
        this.outboxRepository = outboxRepository;
    }

    @Transactional
    public ProductResponse createProduct(CreateProductRequest productRequest) {
        Product product = productMapper.toDomain(productRequest);
        Product savedProduct = productRepository.save(product);

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent(
            savedProduct.getId().value(),
            savedProduct.getSku().value(),
            savedProduct.getName().value(),
            savedProduct.getDescription(),
            savedProduct.getPrice().amount(),
            savedProduct.getPrice().currency(),
            savedProduct.getStatus(),
            savedProduct.getType()
        );

        String eventAsJson = objectMapper.writeValueAsString(productCreatedEvent);
        saveOutboxEvent(savedProduct.getId().value(), "PRODUCT_CREATED", "product.created", eventAsJson);

        return productMapper.toResponse(savedProduct);
    }

    private void saveOutboxEvent(UUID productId, String eventType, String routingKey, String eventAsJson) {
        var outboxEvent = OutboxEvent.pending(
            productId,
            "PRODUCT",
            productId,
            eventType,
            routingKey,
            eventAsJson
        );
        outboxRepository.save(outboxEvent);
    }
}
