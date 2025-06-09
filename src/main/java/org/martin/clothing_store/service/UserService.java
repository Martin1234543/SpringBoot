package org.martin.clothing_store.service;



import org.martin.clothing_store.dto.UserRequest;
import org.martin.clothing_store.model.User;
import org.springframework.stereotype.Service;

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