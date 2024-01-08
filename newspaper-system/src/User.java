import java.util.regex.Pattern;


public abstract class User {
    private String username;
    private String password;

    public User(String userName, String password) {
        this.username = userName;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public abstract UserType getUserType();

    public static boolean isValidUsername(String username) {
        return Pattern.matches("^[a-zA-Z0-9_-]{3,16}$", username);
    }

    public static boolean isValidPassword(String password) {
        return Pattern.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$", password);
    }
}
