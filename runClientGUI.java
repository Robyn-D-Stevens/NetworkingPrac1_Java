package za.ac.cput.networkprac2;

/**
 * @author Wilhelm Rothman
 */
public class runClientGUI extends ClientGUI{

    public static void main(String args[]) {
        //construct a ClientGUI object and make it visible
        ClientGUI gui = new ClientGUI();
        gui.setGUI();

        //construct a Client object to execute the client methods/services
        Client client = new Client();
        client.communicate();
    }// end main
}
