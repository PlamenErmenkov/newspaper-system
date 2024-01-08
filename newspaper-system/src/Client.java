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

    public void checkForMessage() {
        while (reader.nextLine() != null) {
            String message = getMessage();
            System.out.println(message);
        }
    }

    public void runLogic() {
        String message;
//        while (true) {
//            message = getMessage();
//            System.out.println(message);
//            System.out.println("before if");
//
//            if(message.trim().isEmpty() || message == null || message.isBlank() || message.equals("\n")) {
//                System.out.println("if entered");
//                //sendMessage(scanner.nextLine());
//                System.out.println("passed");
//                continue;
//            }
//        }

//        while (true) {
//            message = getMessage();
//            System.out.println(message);
//            sendMessage(scanner.nextLine());
//        }

        getAndPrintMessage();
        getAndPrintMessage();
        sendMessage(scanner.nextLine());
        getAndPrintMessage();
        sendMessage(scanner.nextLine());
        getAndPrintMessage();
        getAndPrintMessage();
        getAndPrintMessage();
        getAndPrintMessage();
        sendMessage(scanner.nextLine());
        getAndPrintMessage();
        sendMessage(scanner.nextLine());
        getAndPrintMessage();
        sendMessage(scanner.nextLine());
        getAndPrintMessage();
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 8888);
        client.runLogic();

    }
}
