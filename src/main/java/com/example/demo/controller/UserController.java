package com.example.demo.controller;
import com.example.demo.dto.RoleRequest;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
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

    @GetMapping("/test-role")
    public String testRole(@AuthenticationPrincipal UserDetails userDetails) {
        return "Twoje role: " + userDetails.getAuthorities().toString();
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
    @PostMapping("/show")
    public List<User> showUser() {
        return userService.findAll();
    }
}