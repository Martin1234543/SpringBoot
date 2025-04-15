package services;

import models.Rental;

import java.util.List;

public interface RentalService {
    boolean isVehicleRented(String vehicleId);
    boolean rent(String vehicleId, String userId);
    boolean returnRental(String vehicleId, String userId);
    List<Rental> findAll();
}