package com.davifaustino.musicstore.products.integration;

import com.davifaustino.musicstore.products.IntegrationTests;
import com.davifaustino.musicstore.products.application.outbox.OutboxStatus;
import com.davifaustino.musicstore.products.infrastructure.config.RabbitMQConfig;
import com.davifaustino.musicstore.products.infrastructure.outbound.persistence.MongoProductRepository;
import com.davifaustino.musicstore.products.infrastructure.outbound.persistence.outbox.MongoOutboxRepository;
import com.davifaustino.musicstore.products.infrastructure.outbound.persistence.outbox.OutboxEventEntity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

class CreateProductIntegrationTests extends IntegrationTests {

    @Autowired
    private MongoProductRepository productRepository;

    @Autowired
    private MongoOutboxRepository outboxRepository;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @BeforeEach
    void cleanDatabase() {
        outboxRepository.deleteAll();
        productRepository.deleteAll();
        rabbitAdmin.purgeQueue(RabbitMQConfig.PRODUCT_CREATED_QUEUE_NAME, false);
    }

    @Test
    void shouldCreateVinylProduct() throws Exception {
        String requestBody = """
				{
					"type": "VINYL",
					"name": "Kind of Blue",
					"sku": "vin-001",
					"description": "Classic jazz vinyl",
					"price": 39.90,
					"currency": "USD",
					"details": {
						"artist": "Miles Davis",
						"albumTitle": "Kind of Blue",
						"label": "Columbia",
						"rpm": "RPM_33",
						"discSize": "TWELVE_INCH",
						"pressing": "Reissue",
						"releaseYear": 1959,
						"genre": "Jazz"
					}
				}
				""";

        HttpResponse<String> response = sendCreateProductRequest(requestBody);

        assertThat(response.statusCode())
                .as(response.body())
                .isEqualTo(200);

        assertThat(response.body()).contains("\"id\":\"");
        assertThat(response.body()).contains("\"sku\":\"VIN-001\"");
        assertThat(response.body()).contains("\"name\":\"Kind of Blue\"");
        assertThat(response.body()).contains("\"description\":\"Classic jazz vinyl\"");
        assertThat(response.body()).contains("\"price\":39.90");
        assertThat(response.body()).contains("\"currency\":\"USD\"");
        assertThat(response.body()).contains("\"status\":\"ACTIVE\"");
        assertThat(response.body()).contains("\"type\":\"VINYL\"");
        assertThat(response.body()).contains("\"artist\":\"Miles Davis\"");
        assertThat(response.body()).contains("\"albumTitle\":\"Kind of Blue\"");
        assertThat(response.body()).contains("\"rpm\":\"RPM_33\"");
        assertThat(response.body()).contains("\"discSize\":\"TWELVE_INCH\"");

        assertThat(productRepository.count()).isEqualTo(1);
        assertProductCreatedOutboxEvent(response, "VIN-001", "Kind of Blue", "VINYL");
    }

    @Test
    void shouldCreateAccessoryProduct() throws Exception {
        String requestBody = """
                {
					"type": "ACCESSORY",
					"name": "Guitar Strings",
					"sku": "acc-010",
					"description": "Nickel wound electric guitar strings",
					"price": 12.50,
					"currency": "USD",
					"details": {
						"accessoryType": "Strings",
						"compatibleWith": ["Electric guitar", "Stratocaster"],
						"brand": "Ernie Ball",
						"size": "10-46"
					}
                }
                """;

        HttpResponse<String> response = sendCreateProductRequest(requestBody);

        assertThat(response.statusCode())
                .as(response.body())
                .isEqualTo(200);

        assertThat(response.body()).contains("\"id\":\"");
        assertThat(response.body()).contains("\"sku\":\"ACC-010\"");
        assertThat(response.body()).contains("\"currency\":\"USD\"");
        assertThat(response.body()).contains("\"type\":\"ACCESSORY\"");
        assertThat(response.body()).contains("\"accessoryType\":\"Strings\"");
        assertThat(response.body()).contains("\"compatibleWith\":[\"Electric guitar\",\"Stratocaster\"]");
        assertThat(response.body()).contains("\"brand\":\"Ernie Ball\"");

        assertThat(productRepository.count()).isEqualTo(1);
        assertProductCreatedOutboxEvent(response, "ACC-010", "Guitar Strings", "ACCESSORY");
    }

    @Test
    void shouldPublishProductCreatedOutboxEvent() throws Exception {
        String requestBody = """
                {
					"type": "ACCESSORY",
					"name": "Guitar Strings",
					"sku": "acc-010",
					"description": "Nickel wound electric guitar strings",
					"price": 12.50,
					"currency": "USD",
					"details": {
						"accessoryType": "Strings",
						"compatibleWith": ["Electric guitar", "Stratocaster"],
						"brand": "Ernie Ball",
						"size": "10-46"
					}
                }
                """;

        HttpResponse<String> response = sendCreateProductRequest(requestBody);

        assertThat(response.statusCode())
                .as(response.body())
                .isEqualTo(200);

        UUID productId = UUID.fromString(extractStringProperty(response.body(), "id"));
        Message message = rabbitTemplate.receive(RabbitMQConfig.PRODUCT_CREATED_QUEUE_NAME, 7_000);

        assertThat(message).isNotNull();

        String body = new String(message.getBody(), StandardCharsets.UTF_8);
        assertThat(body).contains(
                "\"eventType\":\"PRODUCT_CREATED\"",
                productId.toString(),
                "ACC-010"
        );

        OutboxEventEntity event = waitForProcessedOutboxEvent();
        assertThat(event.getAggregateId()).isEqualTo(productId);
        assertThat(event.getStatus()).isEqualTo(OutboxStatus.PROCESSED);
        assertThat(event.getPublishedAt()).isNotNull();
    }

    @Test
    void shouldRejectProductWithUnknownType() throws Exception {
        String requestBody = """
                {
					"type": "BOOK",
					"name": "Domain-Driven Design",
					"sku": "book-001",
					"description": "Unsupported product type",
					"price": 49.90,
					"currency": "USD",
					"details": {}
                }
                """;

        HttpResponse<String> response = sendCreateProductRequest(requestBody);

        assertBadRequestWithoutPersistence(response);
    }

    @Test
    void shouldRejectProductWithoutDetails() throws Exception {
        String requestBody = """
                {
					"type": "VINYL",
					"name": "Kind of Blue",
					"sku": "vin-001",
					"description": "Classic jazz vinyl",
					"price": 39.90
                }
                """;

        HttpResponse<String> response = sendCreateProductRequest(requestBody);

        assertBadRequestWithoutPersistence(response);
    }

    @Test
    void shouldRejectProductWithBlankSku() throws Exception {
        String requestBody = """
                {
					"type": "ACCESSORY",
					"name": "Guitar Strings",
					"sku": " ",
					"description": "Nickel wound electric guitar strings",
					"price": 12.50,
					"currency": "USD",
					"details": {
						"accessoryType": "Strings",
						"compatibleWith": ["Electric guitar"],
						"brand": "Ernie Ball",
						"size": "10-46"
					}
                }
                """;

        HttpResponse<String> response = sendCreateProductRequest(requestBody);

        assertBadRequestWithoutPersistence(response);
        assertThat(response.body()).contains("sku must not be blank");
    }

    @Test
    void shouldRejectProductWithNonPositivePrice() throws Exception {
        String requestBody = """
                {
					"type": "ACCESSORY",
					"name": "Guitar Strings",
					"sku": "acc-010",
					"description": "Nickel wound electric guitar strings",
					"price": 0,
					"currency": "USD",
					"details": {
						"accessoryType": "Strings",
						"compatibleWith": ["Electric guitar"],
						"brand": "Ernie Ball",
						"size": "10-46"
					}
                }
                """;

        HttpResponse<String> response = sendCreateProductRequest(requestBody);

        assertBadRequestWithoutPersistence(response);
        assertThat(response.body()).contains("amount must be greater than zero");
    }

    @Test
    void shouldRejectProductWithInvalidDetailsEnum() throws Exception {
        String requestBody = """
                {
					"type": "VINYL",
					"name": "Kind of Blue",
					"sku": "vin-001",
					"description": "Classic jazz vinyl",
					"price": 39.90,
					"currency": "USD",
					"details": {
						"artist": "Miles Davis",
						"albumTitle": "Kind of Blue",
						"label": "Columbia",
						"rpm": "RPM_16",
						"discSize": "TWELVE_INCH",
						"pressing": "Reissue",
						"releaseYear": 1959,
						"genre": "Jazz"
					}
                }
                """;

        HttpResponse<String> response = sendCreateProductRequest(requestBody);

        assertBadRequestWithoutPersistence(response);
    }

    private HttpResponse<String> sendCreateProductRequest(String requestBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/products"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private void assertBadRequestWithoutPersistence(HttpResponse<String> response) {
        assertThat(response.statusCode())
                .as(response.body())
                .isEqualTo(400);
        assertThat(productRepository.count()).isZero();
        assertThat(outboxRepository.count()).isZero();
    }

    private void assertProductCreatedOutboxEvent(HttpResponse<String> response, String expectedSku, String expectedName, String expectedType) {
        UUID productId = UUID.fromString(extractStringProperty(response.body(), "id"));

        assertThat(outboxRepository.findAll())
                .singleElement()
                .satisfies(event -> {
                    assertThat(event.getCorrelationId()).isEqualTo(productId);
                    assertThat(event.getAggregateType()).isEqualTo("PRODUCT");
                    assertThat(event.getAggregateId()).isEqualTo(productId);
                    assertThat(event.getEventType()).isEqualTo("PRODUCT_CREATED");
                    assertThat(event.getRoutingKey()).isEqualTo("product.created");
                    assertThat(event.getStatus()).isIn(OutboxStatus.PENDING, OutboxStatus.PROCESSED);
                    assertThat(event.getAttempts()).isZero();
                    assertThat(event.getOccurredAt()).isNotNull();
                    assertThat(event.getPayload()).contains(
                            "\"productId\":\"" + productId + "\"",
                            "\"sku\":\"" + expectedSku + "\"",
                            "\"name\":\"" + expectedName + "\"",
                            "\"type\":\"" + expectedType + "\""
                    );
                });
    }

    private OutboxEventEntity waitForProcessedOutboxEvent() throws InterruptedException {
        long deadline = System.nanoTime() + Duration.ofSeconds(7).toNanos();

        while (System.nanoTime() < deadline) {
            var events = outboxRepository.findAll();
            if (events.size() == 1 && events.get(0).getStatus() == OutboxStatus.PROCESSED) {
                return events.get(0);
            }

            Thread.sleep(100);
        }

        throw new AssertionError("Expected one processed outbox event");
    }

    private String extractStringProperty(String json, String property) {
        var matcher = Pattern.compile("\"" + property + "\":\"([^\"]+)\"")
                .matcher(json);

        if (!matcher.find()) {
            throw new AssertionError("Missing JSON property: " + property);
        }

        return matcher.group(1);
    }
}
