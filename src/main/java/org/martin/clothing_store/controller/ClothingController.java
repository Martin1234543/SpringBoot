package org.martin.clothing_store.controller;

import lombok.RequiredArgsConstructor;
import org.martin.clothing_store.dto.IdRequest;
import org.martin.clothing_store.dto.QuantityRequest;
import org.martin.clothing_store.model.Clothing;
import org.martin.clothing_store.model.Orders;
import org.martin.clothing_store.model.User;
import org.martin.clothing_store.repository.ClothingRepository;
import org.martin.clothing_store.repository.OrderRepository;
import org.martin.clothing_store.repository.UserRepository;
import org.martin.clothing_store.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ClothingController {

    private final ClothingRepository clothingRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final UserService userService;


    @PostMapping("/clothing")
    public ResponseEntity<Clothing> addClothing(@RequestBody Clothing clothing) {
        clothing.setId(clothing.getId());
        if (clothingRepository.existsById(clothing.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
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
    @PostMapping("/add")
    public ResponseEntity<?> createClothing(@RequestBody QuantityRequest quantityRequest, AuthenticationPrincipal userDetails) {
        Optional<Clothing> clothingOpt = clothingRepository.findById(quantityRequest.getId());
        if (clothingOpt.isPresent()&& clothingOpt.get().isActive()) {
            Clothing clothing = clothingOpt.get();
            clothing.setQuantity(clothingOpt.get().getQuantity()+quantityRequest.getQuantity());
            clothingRepository.save(clothing);
            return ResponseEntity.ok("Quantity added." + clothing.getQuantity());

        }
        return ResponseEntity.notFound().build();
    }
    @PostMapping("/clothing/activate")
    public ResponseEntity<?> activateClothing(@RequestBody IdRequest id, @AuthenticationPrincipal UserDetails userDetails) {
        Optional<Clothing> clothingOpt = clothingRepository.findById(id.getId());
        if (clothingOpt.isPresent()&& !clothingOpt.get().isActive()) {
            Clothing clothing = clothingOpt.get();
            clothing.setActive(true);
            clothingRepository.save(clothing);
            return ResponseEntity.ok("Clothing activated.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping("/show")
    public ResponseEntity<List<Clothing>> getAllClothing() {
        return new ResponseEntity<>(clothingRepository.findAvailableClothing(), HttpStatus.OK);

    }
    @GetMapping("/showAll")
    public ResponseEntity<List<Clothing>>  getAllAvailableClothing(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> user = userRepository.findByLogin(userDetails.getUsername());
        if (user.isPresent() && user.get().isActive()) {

            return ResponseEntity.ok(clothingRepository.findAll());
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
