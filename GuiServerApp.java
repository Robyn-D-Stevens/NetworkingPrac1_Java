package ServerGUI;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import javax.swing.*;
import java.awt.*;

/**
 * @author Radford
 */
public class GuiServerApp extends JFrame {

    private ServerSocket listener;
    private String msg = "";
    private String upCaseMsg = "";
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private JTextArea clientTxtArea = new JTextArea(5, 40);
    private String response = "";

    private JPanel centerPanel = new JPanel();

    // Client connection
    private Socket client;

    //----------------------------------------------------------------------------------
    // Define a constructor in which you construct a ServerSocket object and setup the Gui
    public GuiServerApp() {
        setTitle("Server GUI");
        setSize(500, 200);
        setLayout(new BorderLayout());

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(new JScrollPane(clientTxtArea), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        try {
            listener = new ServerSocket(12345);
            clientTxtArea.append("Server started. Waiting for client...\n");
            listenForClients();
            processClient();
        } catch (IOException e) {
            clientTxtArea.append("Server error.\n");
        }
    }

    // declare a method to listen for client connections
    private void listenForClients() throws IOException {
        client = listener.accept();
        clientTxtArea.append("Client connected.\n");
        getStreams();
    }

    // declare a method to initiate communication streams
    private void getStreams() throws IOException {
        out = new ObjectOutputStream(client.getOutputStream());
        out.flush();
        in = new ObjectInputStream(client.getInputStream());
    }

    // declare a method in which you write data to the client
    private void sendData(String myMsg) throws IOException {
        out.writeObject(myMsg);
        out.flush();
    }

    // declare a method in which you close the streams and the socket connection
    private void closeConnection() throws IOException {
        if (out != null) out.close();
        if (in != null) in.close();
        if (client != null) client.close();
        if (listener != null) listener.close();
    }

    // declare a method in which you continuously read from the client; process the incoming data; and write results back to client.
    public void processClient() {
        try {
            do {
                msg = (String) in.readObject();
                clientTxtArea.append("Client: " + msg + "\n");

                upCaseMsg = msg.toUpperCase();
                sendData(upCaseMsg);

            } while (!msg.equalsIgnoreCase("TERMINATE"));

            clientTxtArea.append("Client disconnected.\n");
            closeConnection();

        } catch (IOException | ClassNotFoundException e) {
            clientTxtArea.append("Connection error.\n");
        }
    }

    public static void main(String[] args) {
        new GuiServerApp();
    }
}