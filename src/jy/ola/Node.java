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
    
    public static int[] window = {-1, -1, -1, -1, -1}; // The window for sliding window algorithm.
    public static long[] timer = new long[5]; // The window timer for the sliding window algorithm.
    private static long lastSend = 0; // Last packet send time.
    private static String lastPkt = ""; // Last packet sent.
    
    public static int[] statistics = new int[2]; // PACKETS SENT total[0], fail[1]
    
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
            
            // If packet protocol = 0 (GBN) and the packet type is data.
            if(Packet.protocol == 0 && type == 0) {
                
                //System.out.println("\n\nPacket ID: " + Packet.getPacketIndexWithPacket(packet));
                
                // If the packet being sent is in the window, just send.
                // If not, do the normal thing.
                if(!windowContains(Packet.getPacketIndexWithPacket(packet))) {
                    
                    //System.out.print("Not in window... ");
                    //System.out.println("| " + window[0] + " | " + window[1] + " | " + window[2] + " | " + window[3] + " | " + window[4] + " |");
                
                    // If window is not full.
                    if(!windowFull()) {
                        
                        //Add a packet to the window and send.
                        int packetIndex = Packet.getPacketIndexWithPacket(packet);
                        windowAdd(packetIndex);

                    } else {
                        
                        boolean resendWindow = true;

                        do {
                        
                            // While the timeout has not been reached and the packet has not been acknowledged, loop.
                            long timeout1 = System.currentTimeMillis() + 500;
                            while(timeout1 > System.currentTimeMillis() && Packet.packets.get(window[0])[1].equals("0")) {

                                // WAIT FOR ACK OR TIMEOUT.

                                try {
                                    Thread.sleep(100);
                                } catch(InterruptedException e) {}
                            }

                            // If the first packet is acknowledged, shift window and send the next packet.
                            if(Packet.packets.get(window[0])[1].equals("1")) {
                                
                                //System.out.println("Acked");
                                
                                long timeout2 = System.currentTimeMillis() + 500;
                                while(timeout2 > System.currentTimeMillis()) {

                                    // WAIT FOR ACK OR TIMEOUT.

                                    try {
                                        Thread.sleep(100);
                                    } catch(InterruptedException e) {}
                                }

                                //windowShift(1);
                                //windowAdd(Packet.getPacketIndexWithPacket(packet));
                                //resendWindow = false;
                                
                                // Check how many consecutive packets have been acknowledged.
                                int numAck = 1;
                                for(int i = 1; i < window.length; i++) {
                                    //System.out.println(window[i]);
                                    if(Packet.packets.get(window[i])[1].equals("1")) {
                                        //System.out.println("ack");
                                        numAck++;
                                    } else {
                                        break;
                                    }
                                }

                                // Shift by the consecutive number of acknowledged packets.
                                windowShift(numAck);
                                
                                windowAdd(Packet.getPacketIndexWithPacket(packet));
                                resendWindow = false;

                            } else {

                                for(int i = 0; i < window.length; i++) {
                                    //System.out.println("\n\nResend: " + window[i]);
                                    send(Packet.packets.get(window[i])[0], 0);
                                }
                                
                                timer[0] = System.currentTimeMillis();

                            }
                        
                        } while(resendWindow);

                    }
                    
                } // If window does not contain packet.
                
//                // If window is not full.
//                if(!windowFull()) {
//
//                    // Add a packet to the window and send.
//                    int packetIndex = Packet.getPacketIndexWithPacket(packet);
//                    windowAdd(packetIndex);
//
//                } else { 
//
//                    // Window is full, start checking if acknowledgments.
//                    
//                    // If the first packet is not acknowledged.
//                    if(Packet.packets.get(window[0])[1].equals("0")) {
//                        
//                        // And the timeout has runout.
//                        long timeout = lastSend + 5000; // 5s = 5000ms; 1s = 1000ms; 0.5s = 500ms; 0.2s = 200ms;
//                        while(timeout > System.currentTimeMillis() && Packet.packets.get(window[0])[1].equals("0")) {
//
//                            // WAIT FOR ACK OR TIMEOUT.
//
//                            try {
//                                Thread.sleep(100);
//                            } catch(InterruptedException e) {}
//                        }
//                        
//                        // If it still = 0, resend.  If it now = 1, must go to next part.
//                        
//                        if(Packet.packets.get(window[0])[1].equals("0")) {
//                            
//                            for(int i = 0; i < window.length; i++) {
//                                send(Packet.packets.get(i)[0], 0);
//                            }
//                                
//                        }
//                        
//                    }
//                    
//                    if(Packet.packets.get(window[0])[1].equals("1")) {
//                    
//                        // If the first packet in the window has been acknowledged.
//                        
//                        int numAck = 0;
//
//                        // Checks how many consecutive packets have been acknowledged.
//                        for(int i = 0; i < window.length; i++) {
//                            if(Packet.packets.get(window[0])[1].equals("1")) {
//                                numAck++;
//                            } else {
//                                break;
//                            }
//                        }
//
//                        // Shift by the consecutive number of acknowledged packets.
//                        windowShift(numAck);
//
//                        // If the window, now, isn't full, add.
//                        if(!windowFull()) {
//
//                            // Add a packet to the window and send.
//                            int packetIndex = Packet.getPacketIndexWithPacket(packet);
//                            windowAdd(packetIndex);
//
//                        } else {
//
//                            // Check if the first packet is not acknowledged and the timeout has run out.
//                            if(Packet.packets.get(window[0])[1].equals("0")) {
//
//                            }
//
//                        }
//                    
//                    }
//
//                }
                
            } // end packet.protocol = GBN and packet.type = data
            
            // If packet protocol == 1 (SAW) and the packet type is data.
            if(Packet.protocol == 1 && type == 0) {

                // If lastSend == 0, send immediately.
                if(lastSend != 0) {
                    
                    // Get the last packets index in the array list.
                    int lastPacketIndex = Packet.getPacketIndexWithPacket(lastPkt);
                    
                    // If the last packet has not been acknowledged.
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
                                statistics[1]++;
                                send(lastPkt, 0);

                            } else {

                                // Last packet has been acknowledged, continue with send.
                                resendLast = false;

                            }

                        } while(resendLast);
                    
                    }

                }
                
            } // end packet.protocol = SAW and packet.type = data
            
            System.out.print("\n" + Packet.getContent(packet)[0] + " ");

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
            if(Packet.protocol == 0 && type == 0) {
                // Something here...
            }
            
            if(Packet.protocol == 1 && type == 0) {
                lastPkt = packet;
                lastSend = System.currentTimeMillis();
            }
            
            statistics[0]++;
            
            if(random >= drop) {
                try {
                    socket.send(new DatagramPacket(send, send.length, remAddr, remPort));
                } catch(IOException e) {
                    Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, e);
                }
            } else {
                if(Packet.protocol == 0 && type == 0) {
                    statistics[1]++;
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
    
    /**
     * Test if the window has any space available.
     * 
     * @return Returns true if the window is full, false if there is space.
     */
    private boolean windowFull() {
        
        for(int i = window.length - 1; i >= 0; i--) {
            if(window[i] == -1) {
                return false;
            }
        }
            
        return true;
        
    } // end windowFull
    
    /**
     * Adds a packet id to the window.
     * 
     * @param id the id of the packet in the packets array.
     * @return Returns true if the add was successful, false if not.
     */
    private boolean windowAdd(int id) {
        
        // If the window is full, can not add.
        if(windowFull()) {
            return false;
        }
        
        for(int i = 0; i < window.length; i++) {
            if(window[i] == -1) {
                window[i] = id;
                timer[i] = System.currentTimeMillis();
                return true;
            }
        }
        
        return false;
        
    } // end windowAdd
    
    /**
     * Shifts packets, out to the left, by some amount.
     * 
     * @param amount the amount of spots to shift.
     * @return Returns true of the shift was successful, false if not.
     */
    private boolean windowShift(int amount) {
        
        if(amount > 0 && amount <= window.length) {
            for(int i = amount; i < window.length; i++) {
                window[i - amount] = window[i];
                timer[i - amount] = timer[i];
            }
            
            int inv = (window.length - 1) - amount;
            
            for(int i = window.length - 1; i > inv; i--) {
                window[i] = -1;
                timer[i] = 0;
            }
        }
        
        return false;
        
    } // end windowShift
    
    /**
     * Returns true if the packet being sent is in the window already.
     * 
     * @param id a packet array id.
     * @return Returns true if the packet is in the window, false if not.
     */
    private boolean windowContains(int id) {
        
        //System.out.println("Checking Packet ID: " + id);
        for(int i = 0; i < window.length; i++) {
            //System.out.println("Checked Packet ID: " + window[i]);
            if(window[i] == id) {
                return true;
            }
        }
        
        return false;
        
    }
    
} // end class Node
