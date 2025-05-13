package com.martin.car_rent.service.impl;

import com.martin.car_rent.model.Rental;
import com.martin.car_rent.model.User;
import com.martin.car_rent.model.Vehicle;
import com.martin.car_rent.repository.RentalRepository;
import com.martin.car_rent.repository.UserRepository;
import com.martin.car_rent.repository.VehicleRepository;
import com.martin.car_rent.service.RentalService;
import com.martin.car_rent.service.VehicleService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final VehicleService vehicleService;

    @Autowired
    public RentalServiceImpl(RentalRepository rentalRepository,
                             VehicleRepository vehicleRepository,
                             UserRepository userRepository,
                             VehicleService vehicleService) {
        this.rentalRepository = rentalRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.vehicleService = vehicleService;

    }

    @Override
    public boolean isVehicleRented(String vehicleId) {
        return rentalRepository.existsByVehicleIdAndReturnDateIsNull(vehicleId);
    }

    @Override
    public Optional<Rental> findActiveRentalByVehicleId(String vehicleId) {
        return rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId);
    }

    @Override
    @Transactional
    public Rental rent(String vehicleId, String userId) {
        if (!vehicleService.isAvailable(vehicleId)) {
            throw new IllegalStateException("Vehicle " + vehicleId + " is not available for rent.");
        }

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new EntityNotFoundException("Vehicle consistency error. ID: " + vehicleId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

        Rental newRental = Rental.builder()
                .id(UUID.randomUUID().toString())
                .vehicle(vehicle)
                .user(user)
                .rentDate(String.valueOf(LocalDateTime.now()))
                .returnDate(null)
                .build();

        return rentalRepository.save(newRental);
    }

    @Override
    public boolean returnRental(String vehicleId, String userId) {
        return rentalRepository.findByVehicleIdAndUserIdAndReturnDateIsNull(vehicleId, userId).isPresent();
    }

    @Override
    public List<Rental> findAll() {
        return rentalRepository.findAll();
    }

}
