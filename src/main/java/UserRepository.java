import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository implements IUserRepository{
    //User user;
    List<User> users = new ArrayList<>();
    String userFile="C:\\Users\\marti\\zadanie0Spring\\src\\main\\java\\users.txt";
    UserRepository(){
        loadFromFile();
    }
    void loadFromFile(){
        try(BufferedReader reader = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = reader.readLine())!=null){
                String[] parts=line.split(";");
                if (parts.length==3){
                    users.add(new User(parts[0], parts[1], parts[2]));
                }
            }
        }catch (IOException e){
            System.out.println("Error"+ e);

        }
    }
    @Override
    public User getUser(String login) {
        try (BufferedReader br = new BufferedReader(new FileReader(userFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts[0].equals(login)) {
                    return new User(parts[0], parts[1], parts[2]);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading user file: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<String> getUsers() {
        List<String> logins= new ArrayList<>();

        for (User user:users){
            logins.add(user.getLogin());
        }

        return logins;
    }

    @Override
    public void save(User user) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(userFile, true))) {
            bw.write(user.getLogin() + ";" + user.getPassword() + ";" + user.getRole() + "\n");
            System.out.println("User saved to file.");
        } catch (IOException e) {
            System.err.println("Error writing to user file: " + e.getMessage());
        }
    }

}



