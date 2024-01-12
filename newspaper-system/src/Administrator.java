public class Administrator extends User {
    public Administrator(String username, String password) {
        super(username, password);
    }

    @Override
    public UserType getUserType() {
        return UserType.ADMINISTRATOR;
    }

    public User createAccount(UserType type, String username, String password) {
        return switch (type) {
            case WRITER -> new Writer(username, password);
            case EDITOR -> new Editor(username, password);
            default -> throw new IllegalArgumentException("Invalid user!");
        };
    }
}
