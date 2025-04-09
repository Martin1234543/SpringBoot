package services;

import models.Rental;
import models.User;
import models.Vehicle;
import repositories.RentalRepository;
import repositories.VehicleRepository;
import repositories.impl.json.RentalJsonRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RentalService {
    RentalRepository rentalRepository;
    VehicleRepository vehicleRepository;
    public RentalService(RentalRepository rentalRepo, VehicleRepository vehicleRepository) {
        this.rentalRepository=rentalRepo;
        this.vehicleRepository=vehicleRepository;
    }
    public boolean rentVehicle(String vehicleId, User user) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        if (vehicleOptional.isEmpty()) {
            System.out.println("Vehicle with given ID does not exist.");
            return false;
        }

        Optional<Rental> existingRent = rentalRepository.findByVehicleIdAndReturnDateIsNull(vehicleId);
        if (existingRent.isPresent()) {
            System.out.println("Vehicle is already rented.");
            return false;
        }

        Rental rental = Rental.builder()
                .id(UUID.randomUUID().toString())
                .vehicleId(vehicleId)
                .userId(user.getId())
                .rentDate(LocalDateTime.now().toString())
                .returnDate(null)
                .build();

        rentalRepository.save(rental);
        System.out.println("Vehicle rented successfully.");
        return true;
    }


    public void returnVehicle(String vehicleId, User user) {
        Optional<Rental> rentalOptional = rentalRepository.findAll().stream()
                .filter(r -> r.getVehicleId().equals(vehicleId)
                        && r.getUserId().equals(user.getId())
                        && (r.getReturnDate() == null || r.getReturnDate().isBlank()))
                .findFirst();

        if (rentalOptional.isPresent()) {
            Rental rental = rentalOptional.get();
            rental.setReturnDate(LocalDateTime.now().toString());
            rentalRepository.save(rental);
            System.out.println("Successfully returned vehicle.");
        } else {
            System.out.println("You can't return this vehicle. It wasn't rented by you or was already returned.");
        }
    }


    public void showAll(){
        rentalRepository.findAll().forEach(Rental::toString);
    }
    }

