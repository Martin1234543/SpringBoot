package org.martin.clothing_store.service;


import org.martin.clothing_store.model.Orders;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderService {

    boolean isClothingAvailable(String clothingId);

    //Optional<Rental> findActiveRentalByVehicleId(String vehicleId);

    Orders buy(String clothingId, String userId, int quantity);

    Orders returnClothing(String clothingId, String userId);

    List<Orders> findAll();
}