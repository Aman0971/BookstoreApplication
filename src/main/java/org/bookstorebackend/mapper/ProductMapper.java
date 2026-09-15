package org.bookstorebackend.mapper;
import org.bookstorebackend.dto.request.ProductRequestDTO;
import org.bookstorebackend.dto.response.ProductResponseDTO;
import org.bookstorebackend.entity.Product;
import org.springframework.stereotype.Component;

    @Component
    public class ProductMapper {

        public Product toEntity(ProductRequestDTO request) {

            return Product.builder()
                    .bookName(request.getBookName())
                    .author(request.getAuthor())
                    .description(request.getDescription())
                    .price(request.getPrice())
                    .quantity(request.getQuantity())
                    .bookImage(request.getBookImage())
                    .build();
        }

        public ProductResponseDTO toResponse(Product product) {

            return ProductResponseDTO.builder()
                    .id(product.getId())
                    .bookName(product.getBookName())
                    .author(product.getAuthor())
                    .description(product.getDescription())
                    .price(product.getPrice())
                    .quantity(product.getQuantity())
                    .bookImage(product.getBookImage())
                    .build();
        }

        public void updateEntity(
                Product product,
                ProductRequestDTO request) {

            product.setBookName(request.getBookName());
            product.setAuthor(request.getAuthor());
            product.setDescription(request.getDescription());
            product.setPrice(request.getPrice());
            product.setQuantity(request.getQuantity());
            product.setBookImage(request.getBookImage());
        }
    }
