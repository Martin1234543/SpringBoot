import java.util.List;

public interface IUserRepository {
    User getUser(String login);
    List<String> getUsers();
    void save(User user);
}
