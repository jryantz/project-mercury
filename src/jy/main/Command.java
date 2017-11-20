package jy.main;

import jy.ola.Node;
import jy.ola.Packet;
import jy.tools.Convert;

/**
 *
 * @author jonathanyantz
 */

public class Command {
    
    public static void run(String command) {
        
        String[] args = command.split(" ");
        
        switch(args[0]) {
            case "list":
                list();
                break;
            case "send":
                send(args[1]);
                break;
            case "quit":
                quit();
                break;
            default:
                break;
        }
        
    } // end run
    
    private static void send(String packet) {
        
        Main.n.send(Packet.single(Convert.toBinary(packet.getBytes())));
        
    } // end send
    
    private static void list() {
        
        System.out.println("list: list all command options");
        System.out.println("send [String packet]: send a message with the node");
        System.out.println("quit: exit the program");
        
    } // end list
    
    private static void quit() {
        
        System.exit(0);
        
    }
    
} // end class Command
