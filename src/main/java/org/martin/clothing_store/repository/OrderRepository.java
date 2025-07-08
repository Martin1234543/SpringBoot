package org.martin.clothing_store.repository;

import org.martin.clothing_store.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
@Repository
public interface OrderRepository extends JpaRepository<Orders, String> {

    // Znajdź wszystkie zamówienia użytkownika
    List<Orders> findByUserId(String userId);

    // Znajdź zamówienie po ID użytkownika i statusie (np. tylko aktywne, złożone)
    Optional<Orders> findByUserIdAndStatus(String userId, String status);

    Optional<Orders> findById(String Id);

//    // Przykład zapytania JPQL do pobrania zamówień z produktami
//    @Query("SELECT o FROM Orders o JOIN FETCH o.user u WHERE u.id = :userId")
//    List<Orders> findAllWithUserByUserId( String userId);
}
