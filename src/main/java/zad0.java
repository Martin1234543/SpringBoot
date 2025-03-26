import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;


public class zad0 {

    public abstract static class Vehicle {
        private final String brand;
        private final String model;
        private final Integer year;
        private final Integer price;
        private String rented;
        private final String id;


        public Vehicle(String brand, String model, Integer year, Integer price, String rented, String id) {
            this.brand = brand;
            this.model = model;
            this.year = year;
            this.price = price;
            this.rented = rented;
            this.id = id;
        }

        public String getBrand() {
            return brand;
        }

        public String getModel() {
            return model;
        }

        public Integer getYear() {
            return year;
        }

        public Integer getPrice() {
            return price;
        }

        public String isRented() {
            return rented;
        }

        public String getId() {
            return id;
        }

        public String toCSV() {
            return brand + ";" + model + ";" + year + ";" + price + ";" + rented + ";" + id;
        }

        @Override
        public String toString() {
//            String respond = "No";
//            if (rented) {
//                respond = "Yes";
//            }
            return "Vehicle: " + brand + " " + model + " Year: " + year + " Price: " + price + ", Rented by: " + rented+", Identification: "+id;
        }

        @Override
        public boolean equals(Object o) {
            if ((o instanceof Car)) {
                Car c = (Car) o;

                return c.getBrand() == this.brand && c.getModel() == this.model && c.getYear() == this.year && c.getPrice() == this.price;
            } else if ((o instanceof Motorcycle)) {
                Motorcycle c = (Motorcycle) o;

                return c.getBrand() == this.brand && c.getModel() == this.model && c.getYear() == this.year && c.getPrice() == this.price;
            }
            return false;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((brand == null) ? 0 : brand.hashCode());
            result = prime * result + ((model == null) ? 0 : model.hashCode());

            long temp;
            temp = Double.doubleToLongBits(price);
            result = prime * result + (int) (temp ^ (temp >>> 32));

            result = prime * result + year;
            return result;
        }


    }

    static class Car extends Vehicle {
        public Car(String brand, String model, Integer year, Integer price, String rented, String id) {
            super(brand, model, year, price, rented, id);
        }


    }

    static class Motorcycle extends Vehicle {
        private String category;

        public String getCategory() {
            return category;
        }

        public Motorcycle(String brand, String model, Integer year, Integer price, String rented, String id, String category) {
            super(brand, model, year, price, rented, id);
            this.category = category;
        }

        @Override
        public String toCSV() {
            return super.toCSV()+";"+category;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;
            Motorcycle c = (Motorcycle) o;
            return Objects.equals(c.getCategory(), this.category);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), category);
        }

        @Override
        public String toString() {
            return super.toString() + ", Category: " + category;
        }
    }

    interface IVehicleRepository {
        void addVehicle(Vehicle vehicle);

        void removeVehicle(String id);

        void rentVehicle(String id, User user);

        void returnVehicle(String id, User user);

        List<Vehicle> getVehicles();

        int hash();

        boolean equalism();

        void save();

        String Hashage(String a);

        Vehicle getVehicle(String id);
    }

    static class VehicleRepository implements IVehicleRepository {
        public List<Vehicle> copy;
        public List<Vehicle> lista;

        public VehicleRepository() {

            this.copy = loadFromFile();
            this.lista = copy;

        }


        public String Hashage(String a) {
            String sha256hex = DigestUtils.sha256Hex(a);
            return sha256hex;
        }

        @Override
        public Vehicle getVehicle(String id) {
            for (Vehicle vehicle : copy) {
                if (vehicle.id.equals(id)) {
                    return vehicle;
                }
            }
            return null;
        }

        public List<Vehicle> loadFromFile() {
            List<Vehicle> vehicles = new ArrayList<>();
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\marti\\zadanie0Spring\\src\\main\\java\\plik.txt"))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 6) {
                        String brand = parts[0];
                        String model = parts[1];
                        int year = Integer.parseInt(parts[2]);
                        int price = Integer.parseInt(parts[3]);
                        String rented = parts[4].equals("null")?"null":parts[4];
                        String id = parts[5];
                        vehicles.add(new Car(brand, model, year, price, rented, id));
                    } else if (parts.length == 7) {
                        String brand = parts[0];
                        String model = parts[1];
                        int year = Integer.parseInt(parts[2]);
                        int price = Integer.parseInt(parts[3]);
                        String rented = parts[4].equals("null")?"null":parts[4];
                        String id = parts[5];
                        String category = parts[6];
                        vehicles.add(new Motorcycle(brand, model, year, price, rented, id, category));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return vehicles;
        }

        @Override
        public void addVehicle(Vehicle vehicle) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\marti\\zadanie0Spring\\src\\main\\java\\plik.txt", true))) {
                writer.write(vehicle.toCSV() + "\n");
                writer.flush(); // Zapewnia, że dane zostaną zapisane od razu
                System.out.println("Vehicle added.");
            } catch (IOException e) {
                System.err.println("Error writing to file: " + e.getMessage());
            }
        }

        @Override
        public void removeVehicle(String id) {
            String filePath = "C:\\Users\\marti\\zadanie0Spring\\src\\main\\java\\plik.txt";

            try {
                List<String> lines = Files.readAllLines(Paths.get(filePath));
                List<String> updatedLines = new ArrayList<>();

                for (String line : lines) {
                    String[] parts = line.split(";");
                    if (parts.length < 6) continue;

                    String vehicleId = parts[parts.length - 1];

                    if (vehicleId.equals(id)) {
                        System.out.println("Usunięto pojazd o ID: " + id);
                        continue;
                    }
                    updatedLines.add(line);
                }

                Files.write(Paths.get(filePath), updatedLines);
                System.out.println("Pojazd o ID '" + id + "' został usunięty.");

            } catch (IOException e) {
                throw new RuntimeException("Błąd podczas edycji pliku: " + e.getMessage(), e);
            }
        }





        @Override
        public void rentVehicle(String id, User user) {

            for (Vehicle vehicle : copy) {
                if (vehicle.id.equals(id)) {
                    if (vehicle.rented=="null") {
                        vehicle.rented = user.getLogin();
                        user.rentedVehicle(vehicle);
                        save();
                        System.out.println("You rented vehicle id: " + id + ".");
                    } else {
                        System.out.println("Vehicle id: " + id + " is already rented.");
                    }
                    return;
                }
            }
            System.out.println("Vehicle id: " + id + " not found.");
        }

        @Override
        public void returnVehicle(String id, User user) {
            for (Vehicle vehicle : copy) {
                if (vehicle.id.equals(id)) {
                    if (vehicle.rented!="null"&&vehicle.rented.equals(user.getLogin())) {
                        vehicle.rented = "null";
                        user.returnVehicle(vehicle);
                        save();
                        System.out.println("You returned vehicle id: " + id + ".");
                    } else {
                        System.out.println("Cannot return vehicle.");
                    }
                    return;
                }
            }
            System.out.println("Vehicle id: " + id + " not found.");
        }

        @Override
        public List<Vehicle> getVehicles() {

            return copy;
        }

        @Override
        public int hash() {
            System.out.println("Select id of the Vehicle you want to hash.");
            Scanner scanner=new Scanner(System.in);

            return  copy.get(scanner.nextInt()).hashCode();
        }

        @Override
        public boolean equalism() {
            System.out.println("Select id's of the Vehicles you want to compare:");
            Scanner scanner= new Scanner(System.in);
            int first= scanner.nextInt();
            int second = scanner.nextInt();
            return copy.get(first).equals(copy.get(second));
        }

        @Override
        public void save() {
            try (PrintWriter printWriter = new PrintWriter(new FileWriter("C:\\Users\\marti\\zadanie0Spring\\src\\main\\java\\plik.txt"))) {
                for (Vehicle vehicle : copy) {
                    printWriter.println(vehicle.toCSV());
                }
            } catch (IOException e) {
                System.err.println("Error: " + e.getMessage());
            }
        }
    }

    static class App {
        private IVehicleRepository repository;
        IUserRepository userRepository;
        Authentication authentication;
        private User loggedUser;

        public App() {
            this.repository = new VehicleRepository();
            this.userRepository= new UserRepository();
            this.authentication= new Authentication(userRepository);
        }
        private void addUser(Scanner scanner) {
            System.out.println("Enter new user login:");
            String login = scanner.nextLine();

            // Sprawdzenie, czy użytkownik już istnieje
            if (userRepository.getUser(login) != null) {
                System.out.println("User already exists!");
                return;
            }

            System.out.println("Enter password:");
            String password = scanner.nextLine();
            System.out.println("Enter role (admin/user):");
            String role = scanner.nextLine().toLowerCase();

            if (!role.equals("admin") && !role.equals("user")) {
                System.out.println("Invalid role! Must be 'admin' or 'user'.");
                return;
            }

            // Hashowanie hasła
            String hashedPassword = Authentication.hashPassword(password);

            // Tworzenie użytkownika
            User newUser = new User(login, hashedPassword, role);

            // Zapisywanie użytkownika do pliku
            userRepository.save(newUser);

            System.out.println("User " + login + " added successfully!");
        }


        public void run() {
            int input = 0;
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter login:");

            String login=scanner.nextLine();
            if(login.equals("1")){
                System.out.println(DigestUtils.sha256Hex("user"));
            }
            System.out.println("Enter password:");
            String password=scanner.nextLine();
            loggedUser=authentication.login(login, password);

            if(loggedUser!=null) {
                System.out.println("Welcome " + loggedUser.getLogin() + ".");

                String id;
                while (input != 8) {
                    System.out.println("1. Rent vehicle");
                    System.out.println("2. Return Vehicle");
                    System.out.println("3. Show available vehicles");
                    if (loggedUser.getRole().equals("admin")) {
                        System.out.println("4. Add vehicle");
                        System.out.println("5. Remove vehicle");
                        System.out.println("6. Show all users");
                        System.out.println("7. Add user");
                    }
                    System.out.println("8. Exit");

                    input = Integer.parseInt(scanner.nextLine());
                    if (input == 1) {
                        System.out.println("Choose vehicle id to rent:");
                        id = scanner.nextLine();
                        repository.rentVehicle(id, loggedUser);


                    } else if (input == 2) {
                        System.out.println("Choose vehicle id to return:");
                        id = scanner.nextLine();
                        repository.returnVehicle(id, loggedUser);
                    } else if (input == 3) {
                        repository=new VehicleRepository();
                        List<Vehicle> vehicles=(repository.getVehicles());
                        for (Vehicle vehicle:vehicles){
                                System.out.println(vehicle);

                        }
                    } else if (input == 8) {
                        System.out.println("Bye!");
                    } else if (input == 4 && loggedUser.getRole().equals("admin")) {
                        System.out.println("What brand of vehicle do you want to add?");
                        String brand = scanner.nextLine();
                        System.out.println("What is the model?");
                        String model = scanner.nextLine();
                        System.out.println("What is the year of production?");
                        Integer year = Integer.valueOf(scanner.nextLine());
                        System.out.println("What is the price?");
                        Integer price = Integer.valueOf(scanner.nextLine());
                        System.out.println("What is the id?");
                        String id_ = scanner.nextLine();

                        System.out.println("Is it a motorcycle? (yes/no)");
                        String isMotorcycle = scanner.nextLine().trim().toLowerCase();

                        Vehicle vehicle;
                        if (isMotorcycle.equals("yes")) {
                            System.out.println("Enter motorcycle category:");
                            String category = scanner.nextLine();
                            vehicle = new Motorcycle(brand, model, year, price, null, id_, category);
                        } else {
                            vehicle = new Car(brand, model, year, price, null, id_);
                        }

                        repository.addVehicle(vehicle);

                    } else if (input == 5 && loggedUser.getRole().equals("admin")) {
                        System.out.println("Enter id of vehicle to remove: ");
                        String is = scanner.nextLine();
                        repository.removeVehicle(is);
                    } else if (input == 6 && loggedUser.getRole().equals("admin")) {
                        userRepository= new UserRepository();
                        System.out.println(userRepository.getUsers());
                    } else if (input==7&&loggedUser.getRole().equals("admin")) {
                        System.out.println("Enter new user login:");
                        String login_new = scanner.nextLine();

                        // Sprawdzenie, czy użytkownik już istnieje
                        if (userRepository.getUser(login_new) != null) {
                            System.out.println("User already exists!");
                            return;
                        }

                        System.out.println("Enter password:");
                        String password_new = scanner.nextLine();
                        System.out.println("Enter role (admin/user):");
                        String role = scanner.nextLine().toLowerCase();

                        if (!role.equals("admin") && !role.equals("user")) {
                            System.out.println("Invalid role! Must be 'admin' or 'user'.");
                            return;
                        }

                        // Hashowanie hasła
                        String hashedPassword = Authentication.hashPassword(password_new);

                        // Tworzenie użytkownika
                        User newUser = new User(login_new, hashedPassword, role);

                        // Zapisywanie użytkownika do pliku
                        userRepository.save(newUser);

                        System.out.println("User " + login_new + " added successfully!");                    } else {
                    }
                }
            }else {
                System.out.println("Invalid user or password.");
            }
        }
    }

    public static void main(String[] args) {
        new App().run();
    }
}
