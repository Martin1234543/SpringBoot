package app;

import db.JdbcConnectionManager;
import repositories.RentalRepository;
import repositories.UserRepository;
import repositories.VehicleRepository;
import repositories.impl.jdbc.RentalJdbcRepository;
import repositories.impl.jdbc.UserJdbcRepository;
import repositories.impl.json.RentalJsonRepository;
import repositories.impl.jdbc.VehicleJdbcRepository;
import repositories.impl.json.UserJsonRepository;
import repositories.impl.json.VehicleJsonRepository;
import services.AuthService;
import services.RentalService;
import services.VehicleService;

import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        System.out.println("Choose storage type(1-json/2-database)");

        String storageType="";
        String choose= scanner.nextLine();

        if(choose.equals("1")) {
            storageType = "json";
        } else if (choose.equals("2")) {
            storageType="jdbc";
        }
        //TODO: Zmiana typu storage w zaleznosci od parametru przekazanego do programu
        //TODO: Utworzenie RentalJdbcRepository implementujacej RentalRepository
        //TODO: Utworzenie UserJdbcRepository implementujacej UserRepository

        //TODO: Dorzucenie do projektu swoich jsonrepo.

        UserRepository userRepo;
        VehicleRepository vehicleRepo;
        RentalRepository rentalRepo;

        switch (storageType) {
            case "jdbc" -> {
                userRepo = new UserJdbcRepository();
                vehicleRepo = new VehicleJdbcRepository();
                rentalRepo = new RentalJdbcRepository();
            }
            case "json" -> {
                userRepo = new UserJsonRepository();
                vehicleRepo = new VehicleJsonRepository();
                rentalRepo = new RentalJsonRepository();
            }
            default -> throw new IllegalArgumentException("Unknown storage type: " + storageType);
        }
        //TODO:Przerzucenie logiki wykorzystującej repozytoria do serwisów
        AuthService authService = new AuthService(userRepo);
        //TODO:W VehicleService mozna wykorzystac rentalRepo dla wyszukania dostepnych pojazdow
        VehicleService vehicleService = new VehicleService(vehicleRepo, rentalRepo);
        RentalService rentalService = new RentalService(rentalRepo, vehicleRepo);

        //TODO:Przerzucenie logiki interakcji z userem do App
        App app = new App(authService, vehicleService, rentalService);
        app.run();

    }
}