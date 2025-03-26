import org.apache.commons.codec.digest.DigestUtils;

public class Authentication {
    String login;
    String password;
    private final IUserRepository userRepository;

    public Authentication(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }
    static String hashPassword(String password){
        return DigestUtils.sha256Hex(password);
    }
    User login(String login, String password){
        User user = userRepository.getUser(login);
        if(user!=null&&user.getPassword().equals(hashPassword(password))){
            return user;
        }
        return null;
    }

}
