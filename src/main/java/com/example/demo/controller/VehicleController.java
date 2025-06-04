package com.example.demo.controller;




import com.example.demo.dto.RentalRequest;
import com.example.demo.model.Vehicle;
import com.example.demo.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/add")
    public ResponseEntity<Vehicle> addVehicle(@RequestBody Vehicle vehicle) {
        System.out.println(vehicle);
        try {
            Vehicle savedVehicle = vehicleService.save(vehicle);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedVehicle);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

    @PostMapping("/available")
    public ResponseEntity<List<Vehicle>> showAvailableVehicles() {
        List<Vehicle> vehicles = vehicleService.findAvailableVehicles();
        return ResponseEntity.ok(vehicles);
    }

    // Inne, np. pobranie wszystkich
    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        try {
            List<Vehicle> vehicles = vehicleService.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(vehicles);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // Inne, np. pobranie wszystkich
    @GetMapping("/active")
    public ResponseEntity<List<Vehicle>> getAllActiveVehicles() {
        List<Vehicle> vehicles = vehicleService.findAllActive();
        return ResponseEntity.ok(vehicles);
    }



}



