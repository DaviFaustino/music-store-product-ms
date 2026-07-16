package com.davifaustino.musicstore.products.application;

import org.springframework.stereotype.Service;

import com.davifaustino.musicstore.products.application.dtos.CreateProductRequest;
import com.davifaustino.musicstore.products.application.dtos.ProductResponse;
import com.davifaustino.musicstore.products.domain.Product;
import com.davifaustino.musicstore.products.infrastructure.outbound.persistence.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(
            ProductRepository productRepository,
            ProductMapper productMapper
    ) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponse createProduct(CreateProductRequest productRequest) {
        Product product = productMapper.toDomain(productRequest);
        Product savedProduct = productRepository.save(product);

        return productMapper.toResponse(savedProduct);
    }
}
