import java.util.ArrayList;
import java.util.List;

public class User {
    String login, password;
    String role;
    List<zad0.Vehicle> rented;

    public User(String login, String password, String role) {
        this.rented=new ArrayList<>();
        this.login = login;
        this.password = password;
        this.role = role;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public List<zad0.Vehicle> getRented() {
        return rented;
    }
    void rentedVehicle(zad0.Vehicle vehicle){
        rented.add(vehicle);
    }
    void returnVehicle(zad0.Vehicle vehicle){rented.remove(vehicle);}
    String toCSV(){
        return login+";"+password+";"+role+";"+rented;
    }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", rented=" + rented +
                '}';
    }
}
