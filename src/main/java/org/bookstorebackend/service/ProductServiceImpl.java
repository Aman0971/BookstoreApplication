package org.bookstorebackend.service;

import lombok.RequiredArgsConstructor;

import org.bookstorebackend.dto.request.ProductRequestDTO;
import org.bookstorebackend.dto.response.ProductResponseDTO;
import org.bookstorebackend.entity.Product;
import org.bookstorebackend.exception.ResourceNotFoundException;
import org.bookstorebackend.mapper.ProductMapper;
import org.bookstorebackend.repository.ProductRepository;
import org.bookstorebackend.service.ProductService;

import org.springframework.stereotype.Service;

    @Service
    @RequiredArgsConstructor
    public class ProductServiceImpl implements ProductService {

        private final ProductRepository productRepository;
        private final ProductMapper productMapper;

        @Override
        public ProductResponseDTO addBook(ProductRequestDTO request) {

            Product product = productMapper.toEntity(request);
            Product savedProduct = productRepository.save(product);
            return productMapper.toResponse(savedProduct);
        }

        @Override
        public ProductResponseDTO updateBook(Long productId,
                                             ProductRequestDTO request) {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Product not found with id: " + productId));

            productMapper.updateEntity(product, request);
            Product updatedProduct = productRepository.save(product);
            return productMapper.toResponse(updatedProduct);
        }

        @Override
        public void deleteBook(Long productId) {

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

            productRepository.delete(product);
        }
    }

