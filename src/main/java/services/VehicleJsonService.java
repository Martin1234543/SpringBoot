package services;

import models.Vehicle;
import repositories.RentalRepository;
import repositories.VehicleRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class VehicleJsonService implements VehicleService {
    private final VehicleRepository vehicleRepo;
    private final RentalRepository rentalRepo;

    public VehicleJsonService(VehicleRepository vehicleRepo, RentalRepository rentalRepo) {
        this.vehicleRepo = vehicleRepo;
        this.rentalRepo = rentalRepo;
    }

    @Override
    public List<Vehicle> findAll() {
        return vehicleRepo.findAll();
    }

    @Override
    public Optional<Vehicle> findById(String id) {
        return vehicleRepo.findById(id);
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        return vehicleRepo.save(vehicle);
    }

    @Override
    public List<Vehicle> findAvailableVehicles() {
        return vehicleRepo.findAll().stream()
                .filter(vehicle -> rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicle.getId()).isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAvailable(String vehicleId) {
        return rentalRepo.findByVehicleIdAndReturnDateIsNull(vehicleId).isEmpty();
    }

    @Override
    public void addVehicle(Vehicle vehicle) {
        vehicleRepo.save(vehicle);
    }

    @Override
    public void showAll() {
        vehicleRepo.findAll().forEach(vehicle -> System.out.println(vehicle.getId()+" "+vehicle.getCategory()+" "+vehicle.getBrand()+" "+ vehicle.getModel()));

    }
}
