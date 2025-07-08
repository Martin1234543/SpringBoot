package org.martin.clothing_store.controller;

import lombok.RequiredArgsConstructor;
import org.martin.clothing_store.dto.IdRequest;
import org.martin.clothing_store.model.Clothing;
import org.martin.clothing_store.model.Orders;
import org.martin.clothing_store.model.User;
import org.martin.clothing_store.repository.ClothingRepository;
import org.martin.clothing_store.repository.OrderRepository;
import org.martin.clothing_store.repository.UserRepository;
import org.martin.clothing_store.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ClothingController {

    private final ClothingRepository clothingRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final UserService userService;


    @PostMapping("/clothing")
    public ResponseEntity<Clothing> addClothing(@RequestBody Clothing clothing) {
        clothing.setId(clothing.getId());
        clothing.setActive(clothing.isActive());
        Clothing saved = clothingRepository.save(clothing);
        return ResponseEntity.ok(saved);
    }


    @PostMapping("/clothing/deactivate")
    public ResponseEntity<?> deactivateClothing(@RequestBody IdRequest id, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Clothing> clothingOpt = clothingRepository.findById(id.getId());
        if (clothingOpt.isPresent()&& clothingOpt.get().isActive()) {
            Clothing clothing = clothingOpt.get();
            clothing.setActive(false);
            clothingRepository.save(clothing);
            return ResponseEntity.ok("Clothing deactivated.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
    @GetMapping("/show")
    public ResponseEntity<List<Clothing>> getAllClothing() {
        return ResponseEntity.ok(clothingRepository.findAll());
    }
}
