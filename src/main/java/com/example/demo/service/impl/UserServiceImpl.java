package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import com.example.demo.dto.UserRequest;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;

@Service
    @RequiredArgsConstructor
    public class UserServiceImpl implements UserService {
        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final PasswordEncoder passwordEncoder;



    @Override
    public void register(UserRequest req) {
        if(userRepository.findByLogin(req.getLogin()).isPresent()) {
            throw new IllegalArgumentException("Error...");
        }
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() ->
                        new IllegalStateException("There is no role... ROLE_USER"));
        User u = User.builder()
                .id(UUID.randomUUID().toString())
                .login(req.getLogin())
                .password(passwordEncoder.encode(req.getPassword()))
                .roles(Set.of(userRole))
                .isActive(true)
                .build();
        userRepository.save(u);
    }
    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
    @Override
    public void delete(String login) {
        userRepository.findByLogin(login).ifPresent(userRepository::delete);
    }
    @Override
    public void save(User user1) {
        userRepository.findByLogin(user1.getLogin())
                .ifPresent(user -> {
                    user.setLogin(user.getLogin());
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    userRepository.save(user);
                });
    }

    @Override
    public void setRole(String login, String role){
        userRepository.findByLogin(login).ifPresent(user -> {
            user.setRoles(Set.of(roleRepository.findByName(role).orElseThrow()));
        });
    }


    @Override
    public void changeRole(String login, String role) {
            ;
    }

    @Override
    public void addRole(String login, String roleName) {
        Optional<User> user = userRepository.findByLogin(login);
        if(user.isEmpty()) {
            throw new IllegalStateException("User " + login + " not found.");
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Role " + roleName + " not found."));
        user.get().getRoles().add(role);
        userRepository.save(user.get());
    }
    @Override
    public void removeRole(String login, String role) {
        Optional<User> user = userRepository.findByLogin(login);
        if(user.isEmpty()) {
            throw new IllegalStateException("User " + login + " not found.");
        }

        Role role1 = roleRepository.findByName(role)
                .orElseThrow(() -> new IllegalStateException("Role " + role + " not found."));
        user.get().getRoles().remove(role1);
        userRepository.save(user.get());
    }

    public void deleteById(String id) {
        Optional<User> user = userRepository.findByIdAndIsActiveTrue(id);
        if(user.isEmpty()) {
            throw new IllegalStateException("User " + id + " not found.");
        } else {
            user.get().setActive(false);
            user.get().getRoles().clear();
            userRepository.save(user.get());
        }
    }

}

