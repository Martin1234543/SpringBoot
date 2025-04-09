package services;

import models.User;
import org.mindrot.jbcrypt.BCrypt;
import repositories.UserRepository;

import java.util.List;
import java.util.Optional;

public class AuthService {

    private final UserRepository userRepo;

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public boolean register(String login, String rawPassword, String role) {
        if (userRepo.findByLogin(login).isPresent()) {
            System.out.println("Użytkownik o tym loginie już istnieje.");
            return false;
        }

        String hashed = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        User user = User.builder()
                .login(login)
                .password(hashed)
                .role(role)
                .build();

        userRepo.save(user);
        System.out.println("Zarejestrowano pomyślnie.");
        return true;
    }

    public Optional<User> login(String login, String rawPassword) {
        return userRepo.findByLogin(login)
                .filter(user -> BCrypt.checkpw(rawPassword, user.getPassword()));
    }
    public void getUsers(){
        List<User> users=userRepo.findAll();
        users.forEach(user -> System.out.println(user.getLogin()));
    }
}
