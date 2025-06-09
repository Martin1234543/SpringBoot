package org.martin.clothing_store.controller;

import org.martin.clothing_store.dto.ClothingRequest;
import org.martin.clothing_store.dto.IdRequest;
import org.martin.clothing_store.model.Cart;
import org.martin.clothing_store.model.User;
import org.martin.clothing_store.repository.UserRepository;
import org.martin.clothing_store.service.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(@RequestBody List<ClothingRequest> clothingRequests,
                                          @AuthenticationPrincipal UserDetails userDetails) {
        String login = userDetails.getUsername();
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Użytkownik nie znaleziony: " + login));

        Cart updatedCart = cartService.addToCart(user.getId(), clothingRequests);
        return new ResponseEntity<>(updatedCart, HttpStatus.OK);
    }
}
