import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Scanner reader;
    private PrintStream writer;
    private Socket socket;
    private Scanner scanner;

    public Client(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            reader = new Scanner(socket.getInputStream());
            writer = new PrintStream(socket.getOutputStream(), true);
            scanner = new Scanner(System.in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage() {
        return reader.nextLine();
    }

    public void getAndPrintMessage() {
        System.out.println(getMessage());
    }

    public void sendMessage(String message) {
        writer.println(message);
    }

    private void startResponseReaderThread() {
        Thread responseThread = new Thread(() -> {
                while (reader.hasNextLine()) {
                    String response = reader.nextLine();
                    System.out.println(response);
                }
        });
        responseThread.start();
    }

    public void runLogic() {
        startResponseReaderThread();
        while (true) {
            String message = scanner.nextLine();
            if (message.equals("exit")) {
                break;
            }

            sendMessage(message);
        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 8888);
        client.runLogic();
    }
}
