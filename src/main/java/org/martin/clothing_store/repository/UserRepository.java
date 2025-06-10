package org.martin.clothing_store.repository;

import org.martin.clothing_store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByLogin(String login);

    Optional<User> findByIdAndIsActiveIsTrue(String id);
    List<User> findAll();
}