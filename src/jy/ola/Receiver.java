package jy.ola;

import java.io.IOException;
import java.net.DatagramPacket;
import jy.tools.Convert;

/**
 *
 * @author jonathanyantz
 */

public class Receiver implements Runnable {
    
    private boolean running = true;
    public boolean server = false;
    public boolean client = false;
    
    /**
     * Starts the receiver thread, allowing the Node to send and receive simultaneously.
     * 
     * <p>Makes sure the receiver should be running, then checks for whether this is for the client or the server.</p>
     * <p>Sets up the receiving socket then waits for a message to be received.</p>
     */
    @Override
    public void run() {
        
        while(running && (server || client)) {
            byte[] receive = new byte[1024];
            
            try {
                DatagramPacket receivePkt = new DatagramPacket(receive, receive.length);
                
                if(server) {
                
                    Node.socket.receive(receivePkt);

                    if(Node.remPort == 0) {
                        Node.waitflip();
                        
                        try {
                            Thread.sleep(250);
                        } catch(InterruptedException e) {}
                        
                        System.out.println("\nClient connected!");
                        Node.remPort = receivePkt.getPort();
                        Node.remAddr = receivePkt.getAddress();
                    }
                    
                }
                
                if(client) {
                    
                    Node.socket.receive(receivePkt);
                    
                }
                
                String messageIn = new String(receivePkt.getData());
                System.out.println(messageIn);
                Packet.unpack(messageIn);
            } catch(IOException e) {}
            
            try {
                Thread.sleep(100);
            } catch(InterruptedException e) {}
        }
        
    } // end run
    
    /**
     * Ends execution of the receiver thread.
     */
    public void quit() {
        
        running = false;
        
    } // end quit
    
} // end class Receiver
