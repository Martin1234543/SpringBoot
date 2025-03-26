package repositories;

import model.User;

import java.util.List;
import java.util.Optional;
public interface UserRepository {
    List<User> findAll();
    Optional<User> findById(String id);
    Optional<User> findByLogin(String login);
    User save(User user);
    void deleteById(String id);
}