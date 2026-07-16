package com.davifaustino.musicstore.products.integration;

import com.davifaustino.musicstore.products.IntegrationTests;
import com.davifaustino.musicstore.products.infrastructure.outbound.persistence.JpaProductRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.assertj.core.api.Assertions.assertThat;

class CreateProductIntegrationTests extends IntegrationTests {

    @Autowired
    private JpaProductRepository productRepository;

    @BeforeEach
    void cleanDatabase() {
        productRepository.deleteAll();
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
        assertThat(response.body()).contains("\"status\":\"ACTIVE\"");
        assertThat(response.body()).contains("\"type\":\"VINYL\"");
        assertThat(response.body()).contains("\"artist\":\"Miles Davis\"");
        assertThat(response.body()).contains("\"albumTitle\":\"Kind of Blue\"");
        assertThat(response.body()).contains("\"rpm\":\"RPM_33\"");
        assertThat(response.body()).contains("\"discSize\":\"TWELVE_INCH\"");

        assertThat(productRepository.count()).isEqualTo(1);
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
        assertThat(response.body()).contains("\"type\":\"ACCESSORY\"");
        assertThat(response.body()).contains("\"accessoryType\":\"Strings\"");
        assertThat(response.body()).contains("\"compatibleWith\":[\"Electric guitar\",\"Stratocaster\"]");
        assertThat(response.body()).contains("\"brand\":\"Ernie Ball\"");

        assertThat(productRepository.count()).isEqualTo(1);
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
    }
}
