package org.bookstorebackend.service.Impl;

import lombok.RequiredArgsConstructor;

import org.bookstorebackend.dto.request.ProductRequestDTO;
import org.bookstorebackend.dto.response.ProductResponseDTO;
import org.bookstorebackend.entity.Product;
import org.bookstorebackend.exception.ResourceNotFoundException;
import org.bookstorebackend.mapper.ProductMapper;
import org.bookstorebackend.repository.ProductRepository;
import org.bookstorebackend.service.ProductService;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
    @RequiredArgsConstructor
    public class ProductServiceImpl implements ProductService {

        private final ProductRepository productRepository;
        private final ProductMapper productMapper;

        @Override
        @CacheEvict(value = "products", allEntries = true)
        public ProductResponseDTO addBook(ProductRequestDTO request) {

            Product product = productMapper.toEntity(request);
            Product savedProduct = productRepository.save(product);
            return productMapper.toResponse(savedProduct);
        }

        @Override
        @CacheEvict(value = "products", allEntries = true)
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
        @CacheEvict(value = "products", allEntries = true)
        public void deleteBook(Long productId) {

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

            productRepository.delete(product);
        }

        @Override
        @Cacheable(value = "products")
        public List<ProductResponseDTO> getAllBooks(){
            return productRepository.findAll()
                    .stream()
                    .map(productMapper::toResponse)
                    .toList();
        }
        @Override
        public List<ProductResponseDTO> searchBooks(String keyword){

          List<Product> products =
                productRepository
                        .findByBookNameContainingIgnoreCaseOrAuthorContainingIgnoreCase(
                                keyword,
                                keyword
                        );

          return products.stream()
                .map(productMapper::toResponse)
                .toList();
        }

    }

