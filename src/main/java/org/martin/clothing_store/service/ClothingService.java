package org.martin.clothing_store.service;



import org.martin.clothing_store.model.Clothing;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

public interface ClothingService {

    List<Clothing> findAll();

    List<Clothing> findAllAvailable();

    Optional<Clothing> findById(String id);
    Optional<Clothing> findByName(String name);
    Clothing save(Clothing clothing);

    //List<Vehicle> findAvailableVehicles();

    List<Clothing> findQuantityIsNull();

    boolean isAvailable(String clothingId);

    //"it should be soft delete"
    void deleteById(String id);
}