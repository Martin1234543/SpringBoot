package org.martin.clothing_store.repository;

import org.martin.clothing_store.model.Clothing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
@Repository
public interface ClothingRepository extends JpaRepository<Clothing, String> {

    // Wszystkie aktywne ubrania
    List<Clothing> findByIsActiveTrue();

    // Jedno aktywne ubranie po ID
    Optional<Clothing> findByIdAndIsActiveTrue(String id);

    // Ubrania aktywne i dostępne w magazynie (ilość > 0)
    @Query("SELECT c FROM Clothing c WHERE c.isActive = true AND c.quantity > 0")
    List<Clothing> findAvailableClothing();

    // Dostępne ubrania z pominięciem wskazanych ID
    @Query("SELECT c FROM Clothing c WHERE c.isActive = true AND c.quantity > 0 AND c.id NOT IN :excludedIds")
    List<Clothing> findAvailableExcluding( Set<String> excludedIds);

    Optional<Clothing> findByName(String name);
}
