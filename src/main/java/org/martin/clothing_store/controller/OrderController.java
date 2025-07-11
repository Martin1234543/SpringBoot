package org.martin.clothing_store.controller;

import org.aspectj.weaver.ast.Or;
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
    public ResponseEntity<?> buyAllFromCart(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> user = userRepository.findByLogin(userDetails.getUsername());
        if (user.isPresent() && user.get().isActive()) {
            Orders orders = orderService.buy(user.get().getId());
            if (orders == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong quantity.");
            }

            return ResponseEntity.ok(orders);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found.");
    }






    @PostMapping("/return")
    public ResponseEntity<Orders>  returnOrder(@RequestBody IdRequest idRequest, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> user = userRepository.findByLogin(userDetails.getUsername());
        if (user.isPresent() && user.get().isActive()) {
            Orders orders= orderService.returnClothing(idRequest.getId(), user.get().getId());
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping("/show")
    public ResponseEntity<List<Orders>> getAllOrders(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> user = userRepository.findByLogin(userDetails.getUsername());
        if (user.isPresent() && user.get().isActive()) {
                return new ResponseEntity<>(orderRepository.findByUserId(user.get().getId()),  HttpStatus.OK);

        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
    @GetMapping("/orders")
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelOrder(@RequestBody IdRequest idRequest, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> user = userRepository.findByLogin(userDetails.getUsername());
        if (user.isPresent() && user.get().isActive()) {
            Optional<Orders> orders= orderRepository.findById(idRequest.getId());
            if (orders.isPresent()) {
                orders.get().setStatus("cancelled");
                orderRepository.save(orders.get());
                return ResponseEntity.status(HttpStatus.OK).body("Order cancelled.");
            }else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order not found.");
            }
        }else{
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
