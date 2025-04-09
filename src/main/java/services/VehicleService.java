package services;

import models.Rental;
import models.Vehicle;
import repositories.RentalRepository;
import repositories.VehicleRepository;

import java.util.List;
import java.util.Optional;

public class VehicleService {
    VehicleRepository vehicleRepository;
    RentalRepository rentalRepository;
    public VehicleService(VehicleRepository vehicleRepo, RentalRepository rentalRepo) {
        this.rentalRepository=rentalRepo;
        this.vehicleRepository=vehicleRepo;
    }
    public List<Vehicle> getAll(){
        return vehicleRepository.findAll();
    }
    public Optional<Vehicle> findById(String id){
        return vehicleRepository.findById(id);
    }
    public void addVehicle(Vehicle vehicle){
        vehicleRepository.save(vehicle);
    }
    public void removeVehicle(String id){
        if (vehicleRepository.findById(id).isPresent()){
            vehicleRepository.deleteById(id);
        }


    }
    public void showAll(){
        vehicleRepository.findAll().forEach(vehicle -> System.out.println(vehicle.getId()+" "+vehicle.getCategory()+" "+vehicle.getBrand()+" "+ vehicle.getModel()));

    }
}
