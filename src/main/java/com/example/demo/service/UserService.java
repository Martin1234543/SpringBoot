package com.example.demo.service;

import com.example.demo.dto.UserRequest;
import com.example.demo.model.Role;
import com.example.demo.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void register(UserRequest req);
    Optional<User> findByLogin(String login);
    List<User> findAll();

    void delete(String login);
    void save(User user);
    void setRole(String login, String role);

    void changeRole(String login, String role);

    void addRole(String login, String role);

    void deleteById(String id);
    void removeRole(String login, String role);
}