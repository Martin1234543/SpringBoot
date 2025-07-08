package org.martin.clothing_store.service;


import org.martin.clothing_store.model.Orders;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderService {

    boolean isClothingAvailable(String clothingId);

    //Optional<Rental> findActiveRentalByVehicleId(String vehicleId);

    Orders buy(String userId);

    Orders returnClothing(String orderId, String userId);

    List<Orders> findAll();
}