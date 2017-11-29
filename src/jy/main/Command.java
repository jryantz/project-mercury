package jy.main;

import java.util.ArrayList;
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
            case "commands":
                commands();
                break;
            case "file-list":
                fileList();
                break;
            case "file-send":
                fileSend(args[1]);
                break;
            case "packet-list":
                packetList();
                break;
//            case "send":
//                send(args[1]);
//                break;
            case "quit":
                quit();
                break;
            default:
                break;
        }
        
    } // end run
    
    private static void commands() {
        
        System.out.println("commands: list all command options");
        System.out.println("file-list: print a list of all files in the current directory");
        System.out.println("file-send [Integer fileId]: sends a file - files can be selected with file-list");
        System.out.println("packet-list: print a list of all packets sent by this node");
//        System.out.println("send [String packet]: send a message with the node");
        System.out.println("quit: exit the program");
        
    } // end list
    
    private static void fileList() {
        
        String[] list = File.list();
        
        for(int i = 0; i < list.length; i++) {
            System.out.println("[" + i + "]: " + list[i]);
        }
        
    }
    
    private static void fileSend(String file) {
        
        String[] list = File.list();
        
        // If users enters something invalid, send the value too high to matter.
        if(!file.matches("^\\d+$")) {
            file = "10000000";
        }
        
        // If the number isn't in the list, fail.
        if(list.length <= Integer.parseInt(file)) {
            return;
        }
        
        Packet.packAll(list[Integer.parseInt(file)]);
        
        int pkts = 0;
        
        for(int i = Packet.position; i < Packet.packets.size(); i++) {
//            System.out.print(i + " ");
//            System.out.println("[" + i + " - " + Packet.packets.get(i)[0].length() + "]: " + Packet.packets.get(i)[0]);
            
            Main.node.send(Packet.packets.get(i)[0], 0);
            
            pkts++;
        }
        
        Packet.position += pkts;
        
    }
    
    private static void packetList() {
        
        ArrayList<String[]> packets = Packet.packets;
        
        for(int i = 0; i < packets.size(); i++) {
            String[] packet = Packet.getContent(packets.get(i)[0]);
            
            String type = (packet[3].equals("00")) ? "DAT" : "ACK";
            String rcvd = "";
            
            if(packet[3].equals("00")) {
                if(packets.get(i)[1].equals("1")) {
                    rcvd = "\u2713";
                } else {
                    rcvd = "x";
                }
            }
            
            //System.out.println(type + " - " + packet[0] + " " + rcvd + "\t\tLength: " + packet[1] + "\t\tContent: " + packet[6]);
            System.out.println(type + " - " + packet[0] + " " + rcvd + "\t\tLength: " + packet[1]);

        }
        
    }
    
//    private static void send(String packet) {
//        
//        if(packet.length() <= 0) {
//            packet = "<no message>";
//        }
//        
//        String binary = Convert.toBinary(packet.getBytes());
//        
//        if(binary.length() > 1024) {
//            System.out.println("Message too large.");
//        } else {
//            Main.node.send(Packet.data(binary), 0);
//        }
//        
//    } // end send
    
    private static void quit() {
        
        System.exit(0);
        
    }
    
} // end class Command
