package jy.ola;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
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

public class Client {
    
    public static DatagramSocket socket;

    public Client() {
        
        Scanner input = new Scanner(new InputStreamReader(System.in));
        
        System.out.print("Your name: ");
        String name = input.nextLine();
        
        System.out.print("Server IP: ");
        String ip = input.nextLine();
        InetAddress addr = null;
        
        try {
            addr = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
        }
        
        System.out.print("Server Port: ");
        int port = input.nextInt();
        
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
        }
        
        System.out.println("Port: " + socket.getLocalPort());
        System.out.println("*** CHAT STARTED ***");
        
        byte[] send;
        
        Receiver thread = new Receiver();
        thread.server = false;
        thread.client = true;
        Thread t = new Thread(thread);
        t.start();
        
        boolean wait = true;
        do {
            
            String message = name + "> " + input.nextLine();
            
            if(message.equalsIgnoreCase(name + "> quit")) {
                socket.close();
                System.exit(0);
            }
            
            send = message.getBytes();
            
            DatagramPacket sendPkt = new DatagramPacket(send, send.length, addr, port);
            
            try {
                socket.send(sendPkt);
            } catch (IOException e) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, e);
            }
            
        } while(wait == true);
        
        thread.quit();
        
    }//end Client
    
}//end class Client
