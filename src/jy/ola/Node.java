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
    
    private static boolean wait = true; // Flips when the client connects to the server.
    private static boolean lock = true; // Locks the send function until the Node has been initialized.
    
    public final int drop; // The percent of packets to drop.
    
    private static long lastSend = 0; // Last packet send time.
    private static String lastPkt = ""; // Last packet sent.
    
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
        
        // Send the first packet to the server to initialize the connection.
        send(Packet.ack(0), 1);
        Packet.packets.clear();
        
    } // end client
    
    /**
     * Sends data to the other connected node.
     * 
     * <p>Allows for sending of messages.</p>
     * 
     * @param packet packet to be sent.
     * @param type the packet type, 0 for data and 1 for acknowledgment.
     */
    public void send(String packet, int type) {
        
        if(!lock) {
            
            // If packet protocol == 1 (SAW) and the packet type is data.
            if(Packet.protocol == 1 && type == 0) {

                // If lastSend == 0, send immediately.
                if(lastSend != 0) {
                    
                    // Get the last packets index in the array list.
                    int lastPacketIndex = Packet.getPacketIndexWithPacket(lastPkt);
                    
                    if(Packet.packets.get(lastPacketIndex)[1].equals("0")) {
                        
                        boolean resendLast = true;
                    
                        do {

                            // While the timeout has not been reached and the packet has not been acknowledged, loop.
                            long timeout = lastSend + 200; // 1s = 1000ms; 0.5s = 500ms; 0.2s = 200ms;
                            while(timeout > System.currentTimeMillis() && Packet.packets.get(lastPacketIndex)[1].equals("0")) {

                                // WAIT FOR ACK OR TIMEOUT.
                                
                                try {
                                    Thread.sleep(100);
                                } catch(InterruptedException e) {}
                            }
                            
                            // Check if last packet has not been acknowledged.
                            if(Packet.packets.get(lastPacketIndex)[1].equals("0")) {

                                // Last packet not acknowledged.
                                lastSend = 0;
                                send(lastPkt, 0);

                            } else {

                                // Last packet has been acknowledged, continue with send.
                                resendLast = false;

                            }

                        } while(resendLast);
                    
                    }

                }
                
            } // end packet.protocol == SAW and packet.type == data
            
            System.out.print(Packet.getContent(packet)[0] + " ");

            byte[] send;
            send = packet.getBytes();
            
            int random = 0;
            
            // If data packet.
            if(type == 0) {
                random = (int)(Math.random() * 99);
            }
            
            // If acknowledgment packet.
            if(type == 1) {
                random = 100;
            }
            
            // SET/RESET THE TIMER FOR DATA PACKETS
            if(type == 0) {
                lastPkt = packet;
                lastSend = System.currentTimeMillis();
            }
            
            if(random >= drop) {
                try {
                    socket.send(new DatagramPacket(send, send.length, remAddr, remPort));
                } catch(IOException e) {
                    Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, e);
                }
            }
            
        }
        
    } // end sender
    
    /**
     * Initializes the receiver.
     * 
     * <p>Starts the receiver thread and allows the thread to continue executing indefinitely.</p>
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
     * <p>Shows a message alerting the user that there has not been a client connection yet;</p>
     * <p>Displays a rotating animation at the end of the string of text.</p>
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
        
        wait = !wait;
        
    } // end waitflip
    
} // end class Node
