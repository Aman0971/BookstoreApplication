package org.bookstorebackend.service;

import org.bookstorebackend.dto.request.ProductRequestDTO;
import org.bookstorebackend.dto.response.ProductResponseDTO;

import java.util.List;

public interface ProductService {
        ProductResponseDTO addBook(ProductRequestDTO request);
        ProductResponseDTO updateBook(
                Long productId,
                ProductRequestDTO request);
        void deleteBook(Long productId);
        List<ProductResponseDTO> getAllBooks();
    }
