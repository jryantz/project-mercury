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
    
    /**
     * Packs the given payload into a packet with the proper flags and markers.
     * 
     * @param protocol the requested protocol, 0 for GBN OR 1 for SAW.
     * @param type the packet type, 0 for DATA OR 1 for ACK.
     * @param payload the data that will be transmitted in the packet.
     */
    private static void pack(int protocol, int type, String payload) {
        
        String out = "";
        
        out += sequence(packets.size()); // add the sequence number
        out += length(payload.length()); // add the length of the payload
        out += flags(protocol, type);
        
        if(payload.length() < 1024) {
            
            payload = rpad(payload, 1024);
            
        }
        
        out += payload;

        if(verify(out)) {
            packets.add(out);
        }
        
    } // end pack
    
    /**
     * Packs an entire file into a string of packets.
     * 
     * @param protocol the requested protocol, 0 for GBN OR 1 for SAW.
     * @param file the file that should be packed into the set of packets.
     */
    public static void packAll(int protocol, String file) {
        
        byte[] content = File.readAll(file);
        String binary = Convert.toBinary(content);
        
        binary = rpad(binary, binary.length() + (1024 - (binary.length() % 1024)));
        
        for(int i = 0; i < (binary.length() / 1024); i++) {
            pack(protocol, 0, binary.substring((i * 1024), (i * 1024) + 1023));
        }
        
    } // end packAll
    
    public static void unpack(String packet) {
        
        String sequence = packet.substring(0, 32);
        String length = packet.substring(32, 42);
        String flags = packet.substring(42, 50);
        String payload = packet.substring(50);
        
        if(decode(length) > 0) {
        
            String content = payload.substring(0, decode(length));

            System.out.println("SEQUENCE: " + sequence);
            System.out.println("SEQUENCE: " + decode(sequence));
            System.out.println("LENGTH: " + length);
            System.out.println("LENGTH: " + decode(length));
            System.out.println("FLAGS: " + flags);
            System.out.println("PAYLOAD: " + payload);
            System.out.println("CONTENT: " + content);
            System.out.println("CONTENT: " + Convert.toText(content));
        
        }
        
    } // end unpack
    
    /**
     * Returns the packets' sequence number in binary.
     * 
     * @param num the sequence number.
     * @return Returns the sequence number, in binary, for packaging.
     */
    private static String sequence(int num) {
        
        String binary = Integer.toBinaryString(num);
        
        binary = lpad(binary, 32);
        
        return binary;
        
    } // end sequence
    
    /**
     * Returns the packets' length in binary.
     * 
     * @param num the packets length, most important for when the packet is less than max length.
     * @return Returns the packets length, in binary, for packaging.
     */
    private static String length(int num) {
        
        String binary = Integer.toBinaryString(num);
        
        binary = lpad(binary, 10);
        
        return binary;
        
    } // end length
    
    /**
     * Returns a binary integer value in base 10.
     * 
     * @param binary a binary value to be converted to base 10.
     * @return Returns the value, in base 10, for decoding.
     */
    private static int decode(String binary) {
        
        return Integer.parseInt(binary, 2);
        
    } // end lengthDecode
    
    /**
     * Returns the desired flags for the packet.
     * 
     * @param protocol the desired protocol being used for this packet.
     * @return Returns the packet string, in binary, for packaging.
     */
    private static String flags(int protocol, int type) {
        
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
        
        switch(type) {
            case 0:
                binary += "00";
                break;
            case 1:
                binary += "11";
                break;
            default:
                break;
        }
        
        binary += "0000"; // mark the packet as 0 for window slots - not necessary for data packet
        
        return binary;
        
    } // end flags
    
    /**
     * Verifies that the packet is the correct length and is truly in binary.
     * 
     * <p>The payload should be 1024 bits and the header should be 50 bits.</p>
     * <p>If the length is not 1074, this method returns an error.</p>
     * 
     * @param bits the supposed bits, to be verified.
     * @return Returns whether or not the packet is valid.
     */
    public static boolean verify(String bits) {
        
        return bits.length() == (1024 + 50) && bits.matches("^[01]+$");
        
    } // end verify
    
    /**
     * Pads the content, to the left, with zero's.
     * 
     * @param content the content to be padded.
     * @param length the desired length after padding.
     * @return Returns the content at the desired length.
     */
    private static String lpad(String content, int length) {
        
        String out = content;
        
        while(out.length() < length) {
            out = "0" + out;
        }
        
        return out;
        
    } // end lpad
    
    /**
     * Pads the content, to the right, with zero's.
     * 
     * @param content the content to be padded.
     * @param length the desired length after padding.
     * @return Returns the content at the desired length.
     */
    private static String rpad(String content, int length) {
        
        String out = content;
        
        while(out.length() < length) {
            out = out + "0";
        }
        
        return out;
        
    } // end rpad
    
    public static String single(String payload) {
        
        packets.clear();
        
        pack(0, payload);
        String packet = packets.get(0);
        
        packets.clear();
        
        return packet;
        
    } // end single
    
    public static String blank() {
        
        packets.clear();
        
        pack(0, "");
        String packet = packets.get(0);

        packets.clear();
        
        return packet;
        
    } // end blank
    
} // end class Packet
