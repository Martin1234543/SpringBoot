package org.martin.clothing_store.service;

import org.martin.clothing_store.dto.ClothingRequest;
import org.martin.clothing_store.model.Cart;
import org.martin.clothing_store.model.Clothing;
import org.martin.clothing_store.repository.CartRepository;
import org.martin.clothing_store.repository.ClothingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ClothingRepository clothingRepository;


    @Autowired
    public CartService(CartRepository cartRepository, ClothingRepository clothingRepository) {
        this.cartRepository = cartRepository;
        this.clothingRepository = clothingRepository;

    }

    public Cart addToCart(String userId, List<ClothingRequest> clothingRequests) {
        Cart cart = cartRepository.findByUserId(userId).orElse(
                Cart.builder()
                        .id(UUID.randomUUID().toString())
                        .userId(userId)
                        .itemsId("")
                        .build()
        );

        Map<String, Integer> currentItems = new LinkedHashMap<>();

        if (cart.getItemsId() != null && !cart.getItemsId().isEmpty()) {
            String[] entries = cart.getItemsId().split(",");
            for (String entry : entries) {
                String[] parts = entry.split(":");
                if (parts.length == 2) {
                    currentItems.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        }

        for (ClothingRequest request : clothingRequests) {
            Clothing clothing = clothingRepository.findById(request.getClothingId())
                    .filter(Clothing::isActive)
                    .orElseThrow(() -> new IllegalArgumentException("Item not available: " + request.getClothingId()));
            if (clothing.getQuantity() < request.getQuantity()) {
                return null;
            }
            currentItems.merge(request.getClothingId(), request.getQuantity(), Integer::sum);
        }

        String newItemsId = currentItems.entrySet().stream()
                .map(e -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining(","));

        cart.setItemsId(newItemsId);
        return cartRepository.save(cart);
    }
}
