package jy.main;

import jy.ola.Packet;
import jy.tools.Convert;
import jy.tools.File;

/**
 *
 * @author jonathanyantz
 */

public class Command {
    
    public static void run(String command) {
        
        String[] args = new String[2];
        
        if(command.contains(" ")) {
            args[0] = command.substring(0, command.indexOf(" "));
            args[1] = command.substring(command.indexOf(" ") + 1);
        } else {
            args[0] = command;
            args[1] = "";
        }
        
        switch(args[0]) {
            case "list-file":
                list();
                break;
            case "commands":
                commands();
                break;
            case "send":
                send(args[1]);
                break;
            case "send-file":
                
                break;
            case "quit":
                quit();
                break;
            default:
                break;
        }
        
    } // end run
    
    private static void send(String packet) {
        
        if(packet.length() <= 0) {
            packet = "<no message>";
        }
        
        String binary = Convert.toBinary(packet.getBytes());
        
        if(binary.length() > 1024) {
            System.out.println("Message too large.");
        } else {
            Main.node.send(Packet.data(binary));
        }
        
    } // end send
    
    private static void list() {
        
        String[] list = File.list();
        
        for(int i = 0; i < list.length; i++) {
            System.out.println("[" + i + "]: " + list[i]);
        }
        
    }
    
    private static void commands() {
        
        System.out.println("file-list: print a list of all files in the current directory");
        System.out.println("commands: list all command options");
        System.out.println("send [String packet]: send a message with the node");
        System.out.println("quit: exit the program");
        
    } // end list
    
    private static void quit() {
        
        System.exit(0);
        
    }
    
} // end class Command
