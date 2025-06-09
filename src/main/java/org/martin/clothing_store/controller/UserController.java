package org.martin.clothing_store.controller;


import org.martin.clothing_store.dto.IdRequest;
import org.martin.clothing_store.dto.RoleRequest;
import org.martin.clothing_store.model.User;
import org.martin.clothing_store.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }
    @PostMapping("/addRole")
    public ResponseEntity<User> addRole(@RequestBody RoleRequest roleRequest) {
        try {
            userService.addRole(roleRequest.getLogin(), roleRequest.getRole());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/removeRole")
    public ResponseEntity<User> removeRole(@RequestBody RoleRequest roleRequest) {
        try {
            userService.removeRole(roleRequest.getLogin(), roleRequest.getRole());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
//
//    @PostMapping("/{userId}/roles/add")
//    public ResponseEntity<String> addRoleToUser(@RequestParam String roleName, @PathVariable String userId) {
//        try {
//            if(roleName == null || roleName.isEmpty()) {
//                return ResponseEntity.badRequest().body("Role name is required!");
//            }
//
//            userService.addToUser(userId, roleName);
//            return ResponseEntity.ok("Role " + roleName + "has been added to user with ID: " + userId);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

//    @PostMapping("/{userId}/roles/remove")
//    public ResponseEntity<String> removeRoleFromUser(@RequestParam String roleName, @PathVariable String userId) {
//        try {
//            if(roleName == null || roleName.isEmpty()) {
//                return ResponseEntity.badRequest().body("Role name is required!");
//            }
//
//            userService.removeRoleFromUser(userId, roleName);
//            return ResponseEntity.ok("Role " + roleName + " has been removed from user with ID: " + userId);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
    @PostMapping("/deleteUser")
    public ResponseEntity<User> deleteUser(@RequestBody IdRequest idRequest) {
        try {
            userService.deleteById(idRequest.getId());

            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/test-role")
    public String testRole(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<User> user = userService.findByLogin(userDetails.getUsername());

        if (user.isPresent()&&user.get().isActive()) {
            return "Twoje role: " + userDetails.getAuthorities();
        }else {
          return("Access denied");
        }

    }


//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteUser(@PathVariable String id) {
//        try {
//            userService.deleteById(id);
//            return ResponseEntity.ok("User with ID: " + id + " deleted.");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
    @PostMapping("/removeUser")
    public ResponseEntity<String> removeUser(@RequestBody String userId) {
        try {
            userService.deleteById(userId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
    @GetMapping("/show")
    public ResponseEntity<List<User>> showUser(@AuthenticationPrincipal UserDetails userDetails ) {

        try {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(users);
    }catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}