package org.martin.clothing_store.controller;

import org.martin.clothing_store.dto.ClothingRequest;
import org.martin.clothing_store.dto.IdRequest;
import org.martin.clothing_store.model.Orders;
import org.martin.clothing_store.model.User;
import org.martin.clothing_store.repository.ClothingRepository;
import org.martin.clothing_store.repository.OrderRepository;
import org.martin.clothing_store.repository.UserRepository;
import org.martin.clothing_store.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    OrderService orderService;
    ClothingRepository clothingRepository;
    UserRepository userRepository;

    public OrderController(ClothingRepository clothingRepository, UserRepository userRepository, OrderService orderService) {
        this.clothingRepository = clothingRepository;
        this.userRepository = userRepository;
        this.orderService = orderService;
    }



    @PostMapping("/return")
    public ResponseEntity<Orders>  returnOrder(@RequestBody IdRequest idRequest, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails.isAccountNonExpired() && userDetails.isEnabled() && userDetails.isCredentialsNonExpired()) {
            String login = userDetails.getUsername();
            User user= userRepository.findByLogin(login).orElseThrow(()->new UsernameNotFoundException("User not found: "+ login));
            Orders orders= orderService.returnClothing(idRequest.getId(), user.getId());
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
