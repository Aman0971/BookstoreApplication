package org.bookstorebackend.service;

import org.bookstorebackend.dto.response.WishlistItemResponseDTO;

import java.util.List;

public interface WishlistService {

    WishlistItemResponseDTO addToWishlist(Long productId);
    void removeFromWishlist(Long productId);
    List<WishlistItemResponseDTO> getWishlistItems();
}
