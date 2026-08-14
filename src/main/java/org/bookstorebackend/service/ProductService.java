package org.bookstorebackend.service;

import org.bookstorebackend.dto.request.ProductRequestDTO;
import org.bookstorebackend.dto.response.ProductResponseDTO;

    public interface ProductService {

        ProductResponseDTO addBook(ProductRequestDTO request);

        ProductResponseDTO updateBook(
                Long productId,
                ProductRequestDTO request);

        void deleteBook(Long productId);
    }
