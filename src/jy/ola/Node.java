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

public class Node {
    
    private static boolean wait = true; // flips when the client connects to the server
    private static boolean lock = true; // locks the send function until the Node has been initialized
    
    public final int drop; // the percent of packets to drop
    
    private final boolean server;
    private final boolean client;
    
    public static DatagramSocket socket;
    
    private static InetAddress locAddr;
    private static int locPort = 0;
    
    public static InetAddress remAddr;
    public static int remPort = 0;

    public Node(int mode, int drop) {
        
        this.drop = drop;
        
        switch(mode) {
            case 0:
                server = true;
                client = false;
                
                server();
                
                break;
            case 1:
                server = false;
                client = true;
                
                client();
                
                break;
            default:
                server = false;
                client = false;
                
                System.exit(0);
                
                break;
        }
        
    } // end Node
    
    /**
     * Initializes the server.
     */
    private void server() {
        
        locPort = 4000;
        
        try {
            socket = new DatagramSocket(locPort);
        } catch(SocketException e) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, e);
        }
        
        try {
            locAddr = InetAddress.getLocalHost();
        } catch(UnknownHostException e) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, e);
        }
        
        System.out.println("IP: " + locAddr.getHostAddress());
        System.out.println("Port: " + locPort);
        System.out.println("*** COMMUNICATION STARTED ***");
        
        receive();
        waiting();
        lock = false;
        
    } // end server
    
    /**
     * Initializes the client.
     */
    private void client() {
        
        Scanner input = new Scanner(new InputStreamReader(System.in));
        
        System.out.print("Server IP: ");
        String ip = input.nextLine();
        
        try {
            remAddr = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, e);
        }
        
        System.out.print("Server Port: ");
        remPort = input.nextInt();
        
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, e);
        }
        
        System.out.println("Port: " + socket.getLocalPort());
        System.out.println("*** COMMUNICATION STARTED ***");
        
        receive();
        lock = false;
        
    } // end client
    
    /**
     * Sends data to the other connected node.
     * 
     * Allows for sending of messages.
     * @param bits packet to be sent.
     */
    public void send(String bits) {
        
        if(!lock && Packet.verify(bits)) {

            byte[] send;
            boolean cont = true;

            do {

                send = bits.getBytes();

                int random = (int)(Math.random() * 99);
                if(random >= drop) {
                    try {
                        socket.send(new DatagramPacket(send, send.length, remAddr, remPort));
                    } catch(IOException e) {
                        Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, e);
                    }
                }

            } while(cont);
        }
        
    } // end sender
    
    /**
     * Initializes the receiver.
     * 
     * Starts the receiver thread and allows the thread to continue executing indefinitely.
     */
    private void receive() {
        
        Receiver thread = new Receiver();
        thread.server = server;
        thread.client = client;
        
        Thread t = new Thread(thread);
        t.start();
        
    } // end receiver
    
    /**
     * Alerts server user that there is no client connection.
     * 
     * Shows a message alerting the user that there has not been a client connection yet;
     * Displays a rotating animation at the end of the string of text.
     */
    private void waiting() {
        
        wait = true;
        String[] phases = {"|", "/", "-", "\\"};
        
        System.out.printf("Waiting for client to connect... |");
        
        do {
            for(String phase : phases) {
                System.out.printf("\b" + phase);
                
                try {
                    Thread.sleep(100);
                } catch(InterruptedException e) {}
            }
        } while(wait);
        
        System.out.printf("\b" + " ");
        
    } // end waiting
    
    /**
     * Flips the value of the wait variable.
     */
    public static void waitflip() {
        
        if(wait) {
            wait = false;
        } else {
            wait = true;
        }
        
    } // end waitflip
    
} // end class Node
