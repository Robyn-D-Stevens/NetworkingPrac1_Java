package za.ac.cput.networkprac2;

/**
 * @author Wilhelm Rothman
 * 
 * Modified Code by Radford
 */

import java.io.*;
import java.net.*;

public class Client  {

    /*  The protected keyword is an access modifier used for attributes, 
        methods and constructors, making them accessible in the same class,
        its subclasses, and the same package.
    */
    protected static ObjectInputStream in;
    protected static ObjectOutputStream out;
    private Socket socket;
    private String response;

    public Client() {
        // Step 1: Connect to the server, obtain a Socket object.
        try {
            socket = new Socket("localhost", 12345); // connect to server
        } catch (IOException e) {
            System.out.println("Unable to connect to server.");
        }
    }//end constructor

    private void getStreams() throws IOException {
        //construct stream objects for data transfer
        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush(); // flush header
        in = new ObjectInputStream(socket.getInputStream());
    }//end method

    public void communicate() {
        // Step 2: The connection has been established - now set up streams
        try {
            getStreams();

            do {
                /* 
                Step 3: Communicate with client. Since this is a GUI-driven client 
                application, it is only necessary to continuously read from the server until
                the value 'terminate' is read. Writing to the server takes place when the
                user clicks on the textfield, so the code to send data is in the actionPerformed method.             
                */
                response = (String) in.readObject();
                ClientGUI.taClient.append("Server: " + response + "\n");

            } while (!response.equalsIgnoreCase("TERMINATE"));

            // Step 4: close down
            closeConnection();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Communication error.");
        }
    }//end method

    private void closeConnection() {
        //close streams and socket
        try {
            if (out != null) out.close();
            if (in != null) in.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            System.out.println("Error closing connection.");
        }
    }//end method
}//end class
