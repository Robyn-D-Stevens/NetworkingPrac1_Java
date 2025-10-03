package za.ac.cput.networkprac2;

/**
 * @author Wilhelm Rothman
 * Modified Code by Radford
 */
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

public class ClientGUI extends JFrame implements ActionListener {

    private JPanel pnlTop;
    private JPanel pnlBottom;
    private JLabel lblClient;
    private JTextField txtClient;
    private JButton btnSend;
    protected static JTextArea taClient;
    /*  The protected keyword is an access modifier used for attributes, 
        methods and constructors, making them accessible in the same class,
        its subclasses, and the same package.
    */  

    public ClientGUI() {
        //construct swing objects
        pnlTop = new JPanel(new FlowLayout());
        pnlBottom = new JPanel(new BorderLayout());

        lblClient = new JLabel("Enter text here:");
        txtClient = new JTextField(20);
        btnSend = new JButton("Send");

        taClient = new JTextArea(10, 30);
        taClient.setEditable(false); // incoming data only
    }

    public void setGUI() {
        //design GUI        
        setTitle("Client GUI");
        setSize(500, 400);
        setLayout(new BorderLayout());

        pnlTop.add(lblClient);
        pnlTop.add(txtClient);
        pnlTop.add(btnSend);

        pnlBottom.add(new JScrollPane(taClient), BorderLayout.CENTER);

        add(pnlTop, BorderLayout.NORTH);
        add(pnlBottom, BorderLayout.CENTER);

        btnSend.addActionListener(this);
        txtClient.addActionListener(this); // allow <enter> key to trigger send

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }//end constructor

    @Override
    public void actionPerformed(ActionEvent e) {
        //what happens when user presses <enter> from textfield?
        String myMsg = txtClient.getText().trim();
        if (!myMsg.isEmpty()) {
            sendData(myMsg);
            txtClient.setText(""); // clear after sending
        }
    }//end method

    private void sendData(String myMsg) {
        //send a text value to the server
        try {
            Client.out.writeObject(myMsg);
            Client.out.flush();
            taClient.append("Me: " + myMsg + "\n");
        } catch (IOException e) {
            taClient.append("Error sending message.\n");
        }
    }//end method
}//end class
