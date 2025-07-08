package org.martin.clothing_store.controller;

import org.martin.clothing_store.dto.ClothingRequest;
import org.martin.clothing_store.dto.IdRequest;
import org.martin.clothing_store.model.Cart;
import org.martin.clothing_store.model.Clothing;
import org.martin.clothing_store.model.User;
import org.martin.clothing_store.repository.CartRepository;
import org.martin.clothing_store.repository.ClothingRepository;
import org.martin.clothing_store.repository.UserRepository;
import org.martin.clothing_store.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final ClothingRepository clothingRepository;
    private final CartRepository cartRepository;
    private final CartService cartService;
    private final UserRepository userRepository;

    public CartController(ClothingRepository clothingRepository, CartRepository cartRepository, CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.clothingRepository = clothingRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody List<ClothingRequest> clothingRequests,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> user = userRepository.findByLogin(userDetails.getUsername());
        if (user.isPresent() && user.get().isActive()) {

            Cart updatedCart = cartService.addToCart(user.get().getId(), clothingRequests);
            if (updatedCart == null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad quantity.");

            return new ResponseEntity<>(updatedCart, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    private Map<String, Integer> parseItems(String itemsId) {
        Map<String, Integer> map = new HashMap<>();
        if (itemsId == null || itemsId.isBlank()) return map;

        String[] pairs = itemsId.split(",");
        for (String pair : pairs) {
            String[] parts = pair.trim().split(":");
            if (parts.length == 2) {
                String id = parts[0].trim();
                try {
                    int quantity = Integer.parseInt(parts[1].trim());
                    map.put(id, quantity);
                } catch (NumberFormatException ignored) {}
            }
        }
        return map;
    }

    private String stringifyItems(Map<String, Integer> map) {
        return map.entrySet().stream()
                .map(e -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining(","));
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> viewCart(@AuthenticationPrincipal UserDetails userDetails) {

        String login = userDetails.getUsername();
        User user = userRepository.findByLogin(login).orElse(null);
        if (user == null) return ResponseEntity.status(401).build();

        Optional<Cart> cartOpt = cartRepository.findByUserId(user.getId());
        if (cartOpt.isEmpty()) return ResponseEntity.ok(Collections.emptyList());

        Map<String, Integer> itemMap = parseItems(cartOpt.get().getItemsId());
        if (itemMap.isEmpty()) return ResponseEntity.ok(Collections.emptyList());

        List<Clothing> clothes = clothingRepository.findAllById(itemMap.keySet());

        List<Map<String, Object>> response = clothes.stream().map(c -> {
            Map<String, Object> item = new HashMap<>();
            item.put("item", c);
            item.put("quantity", itemMap.getOrDefault(c.getId(), 1));
            return item;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestBody Map<String, String> body,
                                                 @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails.isAccountNonExpired() && userDetails.isEnabled() && userDetails.isCredentialsNonExpired()) {
            String clothingId = body.get("clothingId");
            if (clothingId == null || clothingId.isBlank())
                return ResponseEntity.badRequest().body("Missing clothingId");

            String login = userDetails.getUsername();
            User user = userRepository.findByLogin(login).orElse(null);
            if (user == null||!user.isActive()) return ResponseEntity.status(401).build();

            Optional<Cart> cartOpt = cartRepository.findByUserId(user.getId());
            if (cartOpt.isEmpty()) return ResponseEntity.badRequest().body("Cart not found");

            Cart cart = cartOpt.get();
            Map<String, Integer> itemMap = parseItems(cart.getItemsId());

            if (itemMap.containsKey(clothingId)) {
                itemMap.remove(clothingId);
                cart.setItemsId(stringifyItems(itemMap));
                cartRepository.save(cart);
                return ResponseEntity.ok("Item removed from cart");
            } else {
                return ResponseEntity.badRequest().body("Item not in cart");
            }

        } else {
            return ResponseEntity.status(401).build();
        }
    }

}
