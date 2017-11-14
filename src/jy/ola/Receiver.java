package jy.ola;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 *
 * @author jonathanyantz
 */

public class Receiver implements Runnable {
    
    private boolean running = true;
    
    @Override
    public void run() {
        
        while(running && (Main.server || Main.client)) {
            byte[] receive = new byte[1024];
            
            try {
                DatagramPacket receivePkt = new DatagramPacket(receive, receive.length);
                
                if(Main.server) {
                
                    Server.socket.receive(receivePkt);

                    if(Server.remPort == 0) {
                        System.out.println("\nClient connected!");
                        Server.remPort = receivePkt.getPort();
                        Server.remAddr = receivePkt.getAddress();
                    }
                    
                }
                
                if(Main.client) {
                    
                    Client.socket.receive(receivePkt);
                    
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
