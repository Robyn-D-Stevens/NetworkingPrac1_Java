package za.ac.cput.networkingprac1;

/*
 * ClientApp.java
 *
 */

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.Scanner;

/**

 * @author
 */
public class ClientApp {

    Scanner sc = new Scanner(System.in);
    private Socket server;

    /**
     * Creates a new instance of ClientApp
     */
    public ClientApp() {
        // Attempt to establish connection to server
        try {
            // Create socket
            server = new Socket("127.0.0.1", 6666);
        } catch (IOException ioe) {
            System.out.println("IOException: " + ioe.getMessage());
        }
    }

    public void communicate() {
        // The connection has been established - now send/receive.

        try {
            // Step 1: create channels
            ObjectOutputStream out = new ObjectOutputStream(server.getOutputStream());
            out.flush();
            ObjectInputStream in = new ObjectInputStream(server.getInputStream());

            // Step 2: communicate
            
            String msg="";
            while (!msg.equals("exit")) {
                msg = sc.nextLine();
                out.writeObject(msg);
                out.flush();
                String response = (String) in.readObject();
                System.out.println("From SERVER>> " + response);
            }

            // Step 3: close down
            out.close();
            in.close();
            server.close();
        } catch (IOException ioe) {
            System.out.println("IO Exception: " + ioe.getMessage());
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Class not found: " + cnfe.getMessage());
        }
    }

    public static void main(String[] args) {
        ClientApp client = new ClientApp();
        client.communicate();
    }
}