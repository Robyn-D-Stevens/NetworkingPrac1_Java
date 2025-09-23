package za.ac.cput.networkingprac1;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;

/**
 * @author Robyn Stevens 222201789
 */
public class GuiServerApp extends JFrame implements ActionListener {

    private ServerSocket listener;
    private String msg = "";
    private String upCaseMsg = "";
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private JButton exitBtn = new JButton("EXIT");
    private JTextArea clientTxtArea = new JTextArea(5, 40);
    private JPanel topPanel = new JPanel();
    private JPanel centerPanel = new JPanel();

    // Client connection
    private Socket client;

//----------------------------------------------------------------------------------    
    //Define a constructor in which you construct a ServerSocket object and setup the Gui
    public GuiServerApp() {
        super("GUI Server Application");

        try {
            listener = new ServerSocket(6666, 1);
            System.out.println("Server started on port 6666 (single connection mode)");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Unable to start server: " + e.getMessage());
            System.exit(1);
        }

        clientTxtArea.setEditable(false);
        topPanel.add(exitBtn);
        centerPanel.add(new JScrollPane(clientTxtArea));

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);

        exitBtn.addActionListener(this);

        try {
            listenForClients();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    //declare a method to listen for client connections
    private void listenForClients() throws IOException {
        client = listener.accept(); // blocks until a client connects
        clientTxtArea.append(client.getInetAddress().getHostAddress() + " has connected\n");
        getStreams();
        processClient();
    }

    //declare a method to initiate communication streams
    private void getStreams() throws IOException {
        out = new ObjectOutputStream(client.getOutputStream());
        out.flush();
        in = new ObjectInputStream(client.getInputStream());
    }

    //declare a method in which you write data to the client    
    private void sendData(String myMsg) {
        try {
            out.writeObject(myMsg);
            out.flush();
        } catch (IOException e) {
            clientTxtArea.append("Error sending data: " + e.getMessage() + "\n");
        }
    }

    //declare a method in which you continuously read from the client; process the incoming data; and write results back to client.    
    public void processClient() {
        try {
            do {
                msg = (String) in.readObject();
                clientTxtArea.append("From Client >> " + msg + "\n");

                upCaseMsg = msg.toUpperCase();
                sendData("From SERVER >> " + upCaseMsg);

            } while (!msg.equalsIgnoreCase("terminate"));

            clientTxtArea.append("Client has logged off\nServer now closing down\n");
            closeConnection();

        } catch (IOException | ClassNotFoundException e) {
            clientTxtArea.append("Connection error: " + e.getMessage() + "\n");
        }
    }

    //declare a method in which you close the streams and the socket connection    
    private void closeConnection() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (client != null) {
                client.close();
            }
            if (listener != null) {
                listener.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitBtn) {
            closeConnection();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        GuiServerApp RDS = new GuiServerApp();
        RDS.setSize(500, 300);
        RDS.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        RDS.setVisible(true);

    }
}