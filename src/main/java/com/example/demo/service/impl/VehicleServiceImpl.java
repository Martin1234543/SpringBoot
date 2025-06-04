package com.example.demo.service.impl;

import com.example.demo.model.Vehicle;
import com.example.demo.repository.RentalRepository;
import com.example.demo.repository.VehicleRepository;
import com.example.demo.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final RentalRepository rentalRepository;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository,
                              RentalRepository rentalRepository) {
        this.vehicleRepository = vehicleRepository;
        this.rentalRepository = rentalRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> findAll() {
        return vehicleRepository.findAll();
    }

    @Override
    public List<Vehicle> findAllActive() {
        return vehicleRepository.findByIsActiveTrue();
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return vehicleRepository.findByIdAndIsActiveTrue(id);
    }

    @Override
    @Transactional
    public Vehicle save(Vehicle vehicle) {
        if (vehicle.getId() == null || vehicle.getId().isBlank()) {
            vehicle.setId(UUID.randomUUID().toString());
            vehicle.setActive(true);
        }
        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        Set<String> rentedVehicleIds = rentalRepository.findRentedVehicleIds();
        return vehicleRepository.findByIsActiveTrueAndIdNotIn(rentedVehicleIds);
    }

    @Override
    public List<Vehicle> findRentedVehicles() {
        Set<String> rentedVehicleIds = rentalRepository.findRentedVehicleIds();
        return vehicleRepository.findAllById(rentedVehicleIds)
                .stream()
                .filter(Vehicle::isActive)
                .toList();
    }

    @Override
    public boolean isAvailable(String vehicleId) {
        return !rentalRepository.existsByVehicleIdAndReturnDateIsNull(vehicleId);
    }

    @Override
    public void deleteById(String id) {
        vehicleRepository.deleteById(id);
    }
}
