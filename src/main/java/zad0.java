import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


public class zad0 {

    public abstract static class Vehicle {
        private final String brand;
        private final String model;
        private final Integer year;
        private final Integer price;
        private boolean rented;
        private final String id;


        public Vehicle(String brand, String model, Integer year, Integer price, Boolean rented, String id) {
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

        public boolean isRented() {
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
            String respond = "No";
            if (rented) {
                respond = "Yes";
            }
            return "Vehicle: " + brand + " " + model + " Year: " + year + " Price: " + price + ", Rented: " + respond;
        }
        @Override
        public boolean equals(Object o){
            if((o instanceof Car)){
                Car c = (Car) o;

                return c.getBrand() ==this.brand&& c.getModel() ==this.model&& c.getYear() ==this.year&& c.getPrice() ==this.price;
            } else if ((o instanceof Motorcycle)) {
                Motorcycle c = (Motorcycle) o;

                return c.getBrand() ==this.brand&& c.getModel() ==this.model&& c.getYear() ==this.year&& c.getPrice() ==this.price;
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
        public Car(String brand, String model, Integer year, Integer price,Boolean rented,  String id) {
            super(brand, model, year, price,rented,  id);
        }


    }

    static class Motorcycle extends Vehicle {
        private String category;

        public String getCategory() {
            return category;
        }

        public Motorcycle(String brand, String model, Integer year, Integer price, Boolean rented, String id, String category) {
            super(brand, model, year, price,rented, id);
            this.category = category;
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
        void rentVehicle(String id);

        void returnVehicle(String id);

        List<Vehicle> getVehicles();
        int hash();
        boolean equalism();

        void save();
        String Hashage(String a);
    }

    static class VehicleRepository implements IVehicleRepository {
        public List<Vehicle> copy;
        public List<Vehicle> lista;
        public VehicleRepository() {

            this.copy=loadFromFile();
             this.lista=copy;

        }


        public String Hashage(String a) {
            String sha256hex = DigestUtils.sha256Hex(a);
            return sha256hex;
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
                        boolean rented = Boolean.parseBoolean(parts[4]);
                        String id = parts[5];
                        vehicles.add(new Car(brand, model, year, price,rented , id));
                    } else if (parts.length == 7) {
                        String brand = parts[0];
                        String model = parts[1];
                        int year = Integer.parseInt(parts[2]);
                        int price = Integer.parseInt(parts[3]);
                        boolean rented = Boolean.parseBoolean(parts[4]);
                        String id = parts[5];
                        String category = parts[6];
                        vehicles.add(new Motorcycle(brand, model, year, price,rented,  id, category));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return vehicles;
        }

        @Override
        public void rentVehicle(String id) {
            for (Vehicle vehicle : copy) {
                if (vehicle.id.equals(id)) {
                    if (!vehicle.rented) {
                        vehicle.rented = true;
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
        public void returnVehicle(String id) {
            for (Vehicle vehicle : copy) {
                if (vehicle.id.equals(id)) {
                    if (vehicle.rented) {
                        vehicle.rented = false;
                        save();
                        System.out.println("You returned vehicle id: " + id + ".");
                    } else {
                        System.out.println("Vehicle id: " + id + " is not rented.");
                    }
                    return;
                }
            }
            System.out.println("Vehicle id: " + id + " not found.");
        }

        @Override
        public List<Vehicle> getVehicles() {

            return lista;
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
        private final IVehicleRepository repository;

        public App() {
            this.repository = new VehicleRepository();
        }

        public void run() {
            int input = 0;
            Scanner scanner = new Scanner(System.in);
            String id;
            while (input != 7) {
                System.out.println("1. Rent vehicle");
                System.out.println("2. Return Vehicle");
                System.out.println("3. Show available vehicles");
                System.out.println("4. Hash");
                System.out.println("5. Equal");
                System.out.println("6. Hashage");
                System.out.println("7. Exit");

                input = Integer.parseInt(scanner.nextLine());
                if (input == 1) {
                    System.out.println("Choose vehicle id to rent:");
                    id = scanner.nextLine();
                    repository.rentVehicle(id);
                } else if (input == 2) {
                    System.out.println("Choose vehicle id to return:");
                    id = scanner.nextLine();
                    repository.returnVehicle(id);
                } else if (input == 3) {
                    System.out.println(repository.getVehicles());
                } else if (input == 7) {
                    System.out.println("Bye!");
                } else if (input==4) {
                    System.out.println(repository.hash());
                } else if (input==5) {
                    String is=" ";
                    if(!(repository.equalism())){
                        is=" not ";
                    }

                    System.out.println("The elements are"+is+"equal.");
                } else if (input==6) {
                    id=scanner.nextLine();
                    System.out.println(repository.Hashage(id));
                } else {
                    System.out.println("Choose option from 1 to 6 :[");
                }
            }
        }
    }

    public static void main(String[] args) {
        new App().run();
    }
}
