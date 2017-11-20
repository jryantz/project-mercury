package jy.ola;

import java.util.ArrayList;

import jy.tools.Convert;
import jy.tools.File;

/**
 *
 * @author jonathanyantz
 */

public class Packet {
    
    public static ArrayList<String> packets = new ArrayList();
    
    /*
     * Header Size     50 bits
     * Payload Size    1024 bits
     *
     * Data            1024 bits
     * Sequence #      32 bits
     * Length          10 bits
     * Flags           8 bits
     *
     * Flag 1          GBN (00) - SAW (11)
     * Flag 2          Data (00) - ACK (11)
     * Flag 3          Window slots available (0000)
     */
    
    private static void pack(int protocol, String payload) {
        
        String out = "";
        
        out += sequence(packets.size()); // add the sequence number
        out += length(payload.length()); // add the length of the payload
        out += flags(protocol);
        
        if(payload.length() < 1024) {
            
            payload = rpad(payload, 1024);
            
        }
        
        out += payload;

        if(verify(out)) {
            packets.add(out);
        }
        
    } // end pack
    
    public static void packAll(int protocol, String file) {
        
        byte[] content = File.readAll(file);
        String binary = Convert.toBinary(content);
        
        binary = rpad(binary, binary.length() + (1024 - (binary.length() % 1024)));
        
        for(int i = 0; i < (binary.length() / 1024); i++) {
            pack(protocol, binary.substring((i * 1024), (i * 1024) + 1023));
        }
        
    } // end packAll
    
    public static void unpack() {
        
        
        
    } // end unpack
    
    private static String sequence(int num) {
        
        String binary = Integer.toBinaryString(num);
        
        binary = lpad(binary, 32);
        
        return binary;
        
    } // end sequence
    
    private static String length(int num) {
        
        String binary = Integer.toBinaryString(num);
        
        binary = lpad(binary, 10);
        
        return binary;
        
    } // end length
    
    private static String flags(int protocol) {
        
        String binary = "";
        
        switch(protocol) {
            case 0:
                binary += "00";
                break;
            case 1:
                binary += "11";
                break;
            default:
                break;
        }
        
        binary += "00"; // mark the packet as a data packet
        binary += "0000"; // mark the packet as 0 for window slots - not necessary for data packet
        
        return binary;
        
    } // end flags
    
    public static boolean verify(String bits) {
        
        return bits.length() == (1024 + 50) && bits.matches("^[01]+$");
        
    } // end verify
    
    private static String lpad(String content, int length) {
        
        String out = content;
        
        while(out.length() < length) {
            out = "0" + out;
        }
        
        return out;
        
    } // end lpad
    
    private static String rpad(String content, int length) {
        
        String out = content;
        
        while(out.length() < length) {
            out = out + "0";
        }
        
        return out;
        
    } // end rpad
    
} // end class Packet
