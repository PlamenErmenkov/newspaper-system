import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private ServerSocket server;
    private ExecutorService executor;
    private List<User> users;
    private List<Article> articles;

    public Server(int port) {
        try {
            server = new ServerSocket(port);
            executor = Executors.newFixedThreadPool(5);

            users = new ArrayList<>();
            users.add(new Writer("Writer1", "Password1"));
            users.add(new Editor("Editor1", "Password2"));
            users.add(new Administrator("Admin1", "Password3"));

            articles = new ArrayList<>();
            articles.add(new Article("title1", "content1"));
            articles.add(new Article("title2", "content2"));
            articles.add(new Article("title3", "content3"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Server is running...");

        try {
            while (true) {
                Socket client = server.accept();
                System.out.println("A client connected.");
                executor.execute(new ClientHandler(client, (ArrayList<User>) users, (ArrayList<Article>) articles));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(8888);
        server.start();
    }
}
