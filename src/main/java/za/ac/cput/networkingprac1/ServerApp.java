package za.ac.cput.networkingprac1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;

/**
 *
 * @author 
 */
public class ServerApp {

    // Server socket
    private ServerSocket listener;

    // Client connection
    private Socket client;

    /**
     * Creates a new instance of ServerApp
     */
    public ServerApp() {
        // Create server socket
        try {
            listener = new ServerSocket(6666, 1);
        } catch (IOException ioe) {
            System.out.println("IO Exception: " + ioe.getMessage());
        }
    }

    public void listen() {
        // Start listening for client connections
        try {
            System.out.println("Server is listening");
            client = listener.accept();
            System.out.println("Now moving onto processClient");

            processClient();
        } catch (IOException ioe) {
            System.out.println("IO Exception: " + ioe.getMessage());
        }
    }

    public void processClient() {
        // Communicate with the client

        // First step: initiate channels
        try {
            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());

            // Step 2: communicate
            String msg="";
            while (!msg.equals("exit")) {
                msg = (String) in.readObject();
                System.out.println("From CLIENT>> " + msg);
                out.writeObject("Hello " + msg);
                out.flush();
            }

            // Step 3:close down
            out.close();
            in.close();
            client.close();
        } catch (IOException ioe) {
            System.out.println("IO Exception: " + ioe.getMessage());
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Class not found: " + cnfe.getMessage());
        }
    }

    public static void main(String[] args) {
        // Create application
        ServerApp server = new ServerApp();

        // Start waiting for connections
        server.listen();
    }
}