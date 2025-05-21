package com.martin.car_rent.controller;

import com.martin.car_rent.dto.RentalRequest;
import com.martin.car_rent.model.Vehicle;
import com.martin.car_rent.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {
        try {
            Vehicle savedVehicle = vehicleService.save(vehicle);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @PostMapping("/delete")
    public ResponseEntity<String> deleteVehicle(@RequestBody RentalRequest vehicle) {
        try {


            vehicleService.deleteById(vehicle.getVehicleId());

            return ResponseEntity.status(HttpStatus.OK).body("Usunięto pojazd o id: " + vehicle.getVehicleId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @PostMapping("/show")
    public ResponseEntity<Vehicle> showVehicle() {
        try {
            vehicleService.findAvailableVehicles();
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }
}



