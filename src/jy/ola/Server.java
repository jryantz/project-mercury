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
        
        Receiver thread = new Receiver();
        thread.server = true;
        thread.client = false;
        Thread t = new Thread(thread);
        t.start();
        
        String[] phases = {"|", "/", "-", "\\"};
        System.out.printf("Waiting for client to connect... |");
        while(remPort == 0) {
            for(String phase : phases) {
                System.out.printf("\b" + phase);
                
                try {
                    Thread.sleep(100);
                } catch(InterruptedException e) {}
            }
        }
        
        byte[] send;
        
        boolean wait = true;
        do {
            String message = name + "> " + input.nextLine();
            
            if(message.equalsIgnoreCase(name + "> quit")) {
                socket.close();
                System.exit(0);
            }
            
            send = message.getBytes();
            
            DatagramPacket sendPkt = new DatagramPacket(send, send.length, remAddr, remPort);
            
            try {
                socket.send(sendPkt);
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while(wait == true);
        
        thread.quit();
        
    } // end Server
    
} // end class Server
