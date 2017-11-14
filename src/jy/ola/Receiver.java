package jy.ola;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 *
 * @author jonathanyantz
 */

public class Receiver implements Runnable {
    
    private boolean running = true;
    public boolean server = false;
    public boolean client = false;
    
    @Override
    public void run() {
        
        while(running && (server || client)) {
            byte[] receive = new byte[1024];
            
            try {
                DatagramPacket receivePkt = new DatagramPacket(receive, receive.length);
                
                if(server) {
                
                    Node.socket.receive(receivePkt);

                    if(Node.remPort == 0) {
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
            } catch(IOException e) {}
            
            try {
                Thread.sleep(100);
            } catch(InterruptedException e) {}
        }
        
    } // end run
    
    public void quit() {
        
        running = false;
        
    } // end quit
    
} // end class Receiver
