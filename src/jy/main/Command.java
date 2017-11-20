package jy.main;

import jy.ola.Node;

/**
 *
 * @author jonathanyantz
 */

public class Command {
    
    public static void run(String command) {
        
        run(command, null);
        
    } // end run
    
    public static void run(String command, String[] args) {
        
        switch(command) {
            case "list":
                list();
                break;
            case "send":
                send(args[0]);
                break;
            case "quit":
                quit();
                break;
            default:
                break;
        }
        
    } // end run
    
    private static void send(String packet) {
        
        Main.n.send(packet);
        
    } // end send
    
    private static void list() {
        
        System.out.println("list: list all command options");
        System.out.println("send [String packet]: send data with the node");
        System.out.println("quit: exit the program");
        
    } // end list
    
    private static void quit() {
        
        System.exit(0);
        
    }
    
} // end class Command
