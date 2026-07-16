package com.davifaustino.musicstore.products.infrastructure.inbound.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davifaustino.musicstore.products.application.ProductService;
import com.davifaustino.musicstore.products.application.dtos.CreateProductRequest;
import com.davifaustino.musicstore.products.application.dtos.ProductResponse;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody CreateProductRequest productRequest) {
        var productResponse = productService.createProduct(productRequest);
        return ResponseEntity.ok(productResponse);
    }
}
