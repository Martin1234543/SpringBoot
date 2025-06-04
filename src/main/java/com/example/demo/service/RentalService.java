package com.example.demo.service;


import com.example.demo.model.Rental;

import java.util.List;
import java.util.Optional;

public interface RentalService {

    boolean isVehicleRented(String vehicleId);

    Optional<Rental> findActiveRentalByVehicleId(String vehicleId);

    Rental rent(String vehicleId, String userId);

    Rental returnRental(String vehicleId, String userId);

    List<Rental> findAll();
}