package org.martin.clothing_store.controller;

import org.martin.clothing_store.dto.ClothingRequest;
import org.martin.clothing_store.dto.IdRequest;
import org.martin.clothing_store.model.Cart;
import org.martin.clothing_store.model.Clothing;
import org.martin.clothing_store.model.Orders;
import org.martin.clothing_store.model.User;
import org.martin.clothing_store.repository.CartRepository;
import org.martin.clothing_store.repository.ClothingRepository;
import org.martin.clothing_store.repository.OrderRepository;
import org.martin.clothing_store.repository.UserRepository;
import org.martin.clothing_store.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository orderRepository;
    OrderService orderService;
    ClothingRepository clothingRepository;
    UserRepository userRepository;
    CartRepository cartRepository;

    public OrderController(CartRepository cartRepository, ClothingRepository clothingRepository, UserRepository userRepository, OrderService orderService, OrderRepository orderRepository) {
        this.clothingRepository = clothingRepository;
        this.userRepository = userRepository;
        this.orderService = orderService;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
    }
    @PostMapping("/buy")
    public ResponseEntity<Orders> buyAllFromCart(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails.isAccountNonExpired() && userDetails.isEnabled() && userDetails.isCredentialsNonExpired()) {
            String login = userDetails.getUsername();
            User user = userRepository.findByLogin(login)
                    .orElseThrow(() -> new UsernameNotFoundException("Użytkownik nie znaleziony: " + login));
            Orders orders = orderService.buy(user.getId());

            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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
    @GetMapping("/show")
    public ResponseEntity<List<Orders>> getAllOrders(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails.isAccountNonExpired() && userDetails.isEnabled() && userDetails.isCredentialsNonExpired()&&userDetails.isCredentialsNonExpired()) {
            String login = userDetails.getUsername();
            Optional<User> user= userRepository.findByLogin(login);
            if(user.isPresent()){
                return new ResponseEntity<>(orderRepository.findByUserId(user.get().getId()),  HttpStatus.OK);

            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
}
