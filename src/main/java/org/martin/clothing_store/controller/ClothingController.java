package org.martin.clothing_store.controller;

import lombok.RequiredArgsConstructor;
import org.martin.clothing_store.model.Clothing;
import org.martin.clothing_store.model.Orders;
import org.martin.clothing_store.model.User;
import org.martin.clothing_store.repository.ClothingRepository;
import org.martin.clothing_store.repository.OrderRepository;
import org.martin.clothing_store.repository.UserRepository;
import org.martin.clothing_store.service.UserService;
import org.springframework.http.ResponseEntity;
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

    // 🔹 Dodaj nowe ubranie
    @PostMapping("/clothing")
    public ResponseEntity<Clothing> addClothing(@RequestBody Clothing clothing) {
        clothing.setId(UUID.randomUUID().toString());
        clothing.setActive(true);
        Clothing saved = clothingRepository.save(clothing);
        return ResponseEntity.ok(saved);
    }

    // 🔹 Dezaktywuj ubranie
    @PutMapping("/clothing/{id}/deactivate")
    public ResponseEntity<?> deactivateClothing(@PathVariable String id) {
        Optional<Clothing> clothingOpt = clothingRepository.findById(id);
        if (clothingOpt.isPresent()) {
            Clothing clothing = clothingOpt.get();
            clothing.setActive(false);
            clothingRepository.save(clothing);
            return ResponseEntity.ok("Clothing deactivated.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 🔹 Wyświetl wszystkie zamówienia
    @GetMapping("/orders")
    public ResponseEntity<List<Orders>> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    // 🔹 Wyświetl wszystkich użytkowników
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
}
