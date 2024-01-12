import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private Socket client;
    private Scanner reader;
    private PrintStream writer;
    private ArrayList<User> users;
    private ArrayList<Article> articles;

    public ClientHandler(Socket client, ArrayList<User> users, ArrayList<Article> articles) {
            this.client = client;
            this.users = new ArrayList<>(Collections.synchronizedList(users));
            this.articles = new ArrayList<>(Collections.synchronizedList(articles));
    }

    @Override
    public void run() {
        try {
            reader = new Scanner(client.getInputStream());
            writer = new PrintStream(client.getOutputStream(), true);

            sendMessage("Welcome!");
            String username = askUsername();
            String password = askPassword(username);
            User user = processUser(username, password);
            UserType type = user.getUserType();

            switch (type) {
                case WRITER:
                    sendMessage("Successfully logged as writer!");
                    askWriterOptions((Writer) user);
                    break;
                case EDITOR:
                    sendMessage("Successfully logged as editor!");
                    askEditorOptions((Editor) user);
                    break;
                case ADMINISTRATOR:
                    sendMessage("Successfully logged as administrator");
                    askAdministratorOptions((Administrator) user);
                    break;
                default:
                    sendMessage("Something went wrong!");
                    break;
            }

            sendMessage("Thank you for using our system, bye!");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    private String getMessage() {
        return reader.nextLine();
    }

    private void sendMessage(String message) {
        writer.println(message);
    }

    private String askUsername() {
        String username;
        sendMessage("Enter username: ");

        do {
            username = getMessage();
            if (!isUsernameValid(username)) {
                sendMessage("No such username, try again: ");
            }
        } while (!isUsernameValid(username));
        return username;
    }

    private boolean isUsernameValid(String username) {
        return users.stream().anyMatch(user -> user.getUsername().equals(username));
    }

    private String askPassword(String username) {
        String password;
        sendMessage("Enter password: ");

        do {
            password = getMessage();
            if(!isPasswordValid(username, password)) {
                sendMessage("Incorrect password, try again: ");
            }
        } while (!isPasswordValid(username, password));
        return password;
    }

    private boolean isPasswordValid(String username, String password) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }

    private User processUser(String username, String password) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    private void displayWriterOptions() {
        sendMessage("""
                Choose an option:
                1. Create new article.
                2. Send article""");
    }
    private void askWriterOptions(Writer writer) {
        displayWriterOptions();
        String option = getMessage();

        switch (option) {
            case "1":
                handleWriterCreateArticle(writer);
                break;
            case "2":
                handleWriterSendArticle(writer);
                break;
            default:
                sendMessage("Invalid option!");
                break;
        }
    }

    private void handleWriterCreateArticle(Writer writer) {
        sendMessage("Enter title: ");
        String title = getMessage();
        sendMessage("Enter content: ");
        String content = getMessage();

        writer.createArticle(title, content);
        sendMessage("Article created successfully.");
    }

    private void handleWriterSendArticle(Writer writer) {
        sendMessage("Enter title to be send: ");
        String title = getMessage();

        synchronized (articles) {
            articles.add(writer.sendArticle(title));
        }
    }

    private void askEditorOptions(Editor user) {
        displayEditorOptions();
        String option = getMessage();

        switch (option) {
            case "1":
                handleEditorEditArticle(user);
                break;
            case "2":
                handleEditorApproveArticle(user);
                break;
            default:
                sendMessage("Invalid option!");
                break;
        }
    }

    private void displayEditorOptions() {
        sendMessage("""
                Choose an option:
                1. Edit an article.
                2. Look up an article and choose to approve it or not.
                """);
    }

    private synchronized Article getArticle(String title) {
        return articles.stream()
                .filter(article -> article.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }
    private void handleEditorEditArticle(Editor editor) {
        while (true) {
            sendMessage("Enter article's title: ");
            String title = getMessage();

            Article article = getArticle(title);

            if (article != null) {
                sendMessage("Article found.");

                sendMessage("Enter edited content: ");
                String editedContent = getMessage();

                editor.editArticle(article, editedContent);

                break;
            } else {
                sendMessage("No such article found, try again.");
            }
        }
    }

    private void handleEditorApproveArticle(Editor user) {
        while (true) {
            sendMessage("Enter article's title: ");
            String title = getMessage();

            Article article = getArticle(title);

            if (article != null) {
                sendMessage("Article found.");
                sendMessage(user.displayArticle(article));
                sendMessage("\nDo you approve it (y/n): ");
                String approval = getMessage();

                switch (approval) {
                    case "y":
                        user.approveArticle(article);
                        sendMessage("Article approved successfully.");
                        break;
                    case "n":
                        sendMessage("Article not approved.");
                        break;
                    default:
                        sendMessage("Invalid option.");
                        continue;
                }
                break;
            } else {
                sendMessage("No such article found, try again.");
            }
        }
    }

    private void askAdministratorOptions(Administrator user) {
        displayAdministratorOptions();
        String option = reader.nextLine();

        switch (option) {
            case "1":
                handleAdminCreateWriter(user);
                sendMessage("Writer created successfully.");
                break;
            case "2":
                handleAdminCreateEditor(user);
                sendMessage("Editor created successfully.");
                break;
            default:
                sendMessage("Invalid option!");
                break;
        }

    }

    private void displayAdministratorOptions() {
        sendMessage("""
                1. Create new writer account.
                2. Create new editor account.
                """);
    }

    private void handleAdminCreateWriter(Administrator admin) {
        sendMessage("Enter username for the new writer: ");
        String username = getMessage();
        sendMessage("Enter password for the new writer: ");
        String password = getMessage();

        users.add(admin.createAccount(UserType.WRITER, username, password));
    }

    private void handleAdminCreateEditor(Administrator admin) {
        sendMessage("Enter username for the new editor: ");
        String username = getMessage();
        sendMessage("Enter password for the new editor: ");
        String password = getMessage();

        users.add(admin.createAccount(UserType.EDITOR, username, password));
    }

    private void closeResources() {
        try{
            client.close();
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
