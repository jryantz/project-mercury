package jy.ola;

import java.io.IOException;
import java.net.DatagramPacket;

/**
 *
 * @author jonathanyantz
 */

public class Receiver implements Runnable {
    
    boolean running = true;
    
    @Override
    public void run() {
        
        while(running) {
            byte[] receive = new byte[1024];
            
            try {
                DatagramPacket receivePkt = new DatagramPacket(receive, receive.length);
                Server.socket.receive(receivePkt);
                
                if(Server.remPort == 0) {
                    System.out.println("\nClient connected!");
                    Server.remPort = receivePkt.getPort();
                    Server.remAddr = receivePkt.getAddress();
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
