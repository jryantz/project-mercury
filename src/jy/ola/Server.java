package jy.ola;

import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jonathanyantz
 */

public class Server {
    
    public static DatagramSocket socket;
    public static InetAddress remAddr;
    public static int remPort = 0;

    public Server() {
        
        Scanner input = new Scanner(new InputStreamReader(System.in));
        System.out.print("Your name: ");
        String name = input.nextLine();
        
        int port = 4000;
        
        try {
            socket = new DatagramSocket(port);
        } catch(SocketException e) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
        }
        
        try {
            System.out.println("IP: " + InetAddress.getLocalHost().getHostAddress());
        } catch(UnknownHostException e) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, e);
        }
        
        System.out.println("Port: " + port);
        System.out.println("*** CHAT STARTED ***");
        
    }//end Server
    
}//end class Server
