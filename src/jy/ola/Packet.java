package jy.ola;

import java.util.ArrayList;
import jy.main.Main;
import jy.tools.Convert;
import jy.tools.File;

/**
 *
 * @author jonathanyantz
 */

public class Packet {
    
    public static int protocol = 1; // 0 for GBN - 1 for SAW
    
    public static ArrayList<String[]> packets = new ArrayList(); // OUTGOING PACKETS
    private static ArrayList<String[]> buffer = new ArrayList(); // INCOMING PACKETS
    
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
     *
     * -----
     *
     * Packets Array
     *
     * [0] - DATA
     * [1] - ACK ? (0 NOT-ACKNOWLEDGED) : (1 ACKNOWLEDGED)
     *
     */
    
    private static void pack(int type, String payload) {
        
        pack(packets.size(), type, payload);
        
    } // end pack
    
    /**
     * Packs the given payload into a packet with the proper flags and markers.
     * 
     * @param sequence the desired sequence number.
     * @param type the packet type, 0 for DATA OR 1 for ACK.
     * @param payload the data that will be transmitted in the packet, must be in binary.
     */
    private static void pack(int sequence, int type, String payload) {
        
        String out = "";
        
        out += sequence(sequence); // Add the sequence number.
        
        if(payload.length() < 1024) {
            out += length(payload.length()); // Add the length of the payload - translate actual size if length is less than max.
        }
        
        if(payload.length() == 1024) {
            out += length(payload.length() - 1); // Add the length of the payload - subtract 1 to keep packet length valid.
        }
        
        out += flags(protocol, type); // Add the flags to the packet.
        
        if(payload.length() < 1024) {
            payload = rpad(payload, 1024);
        }
        
        out += payload;
        
        String[] arr = new String[2];
        arr[0] = out;
        arr[1] = "0";

        if(verify(out)) {
            packets.add(arr);
        }
        
    } // end pack
    
    /**
     * Packs an entire file into a string of packets.
     * 
     * @param file the file that should be packed into the set of packets.
     */
    public static void packAll(String file) {
        
        byte[] content = File.readAll(file);
        String binary = Convert.toBinary(content);
        
//        System.out.println(binary.length());
//        System.out.println("\n" + binary);
        
        for(int i = 0; i < binary.length(); i += 1024) {
            if((i + 1024) > binary.length()) {
                pack(0, binary.substring(i));
            } else {
                pack(0, binary.substring(i, i + 1024));
            }
        }
        
    } // end packAll
    
    public static void unpack(String packet) {
        
        String[] content = getContent(packet);
        
        if(content[3].equals("00")) {
            
            // Send the acknowledgment.
            Main.node.send(ack(Integer.parseInt(content[0])), 1);
            
            // Add the data for the file to the out buffer.
            String[] out = new String[2];
            out[0] = content[0];
            out[1] = content[5];
            buffer.add(out);
            
            // If the length is less than 1024 - write the file.
            if(Integer.parseInt(content[1]) <= 1024) {
                File.writeAll("COSC635_P2_DataReceived.txt", File.convert(buffer));
            }
        
        }
        
        if(content[3].equals("11") && !packets.isEmpty()) {
            
            packets.get(Integer.parseInt(content[0]))[1] = "1";
            //System.out.println("Packet #" + content[0] + ", acknowledged.");
            
        }
        
    } // end unpack
    
    public static String[] getContent(String packet) {
        
        String[] out = new String[7];
        
        String sequence = packet.substring(0, 32);
        out[0] = "" + decode(sequence);
        
        String length = packet.substring(32, 42);
        out[1] = "" + (decode(length) + 1);
        
        String flags = packet.substring(42, 50);
        
        String protocol = flags.substring(0, 2);
        out[2] = protocol;
        
        String type = flags.substring(2, 4);
        out[3] = type;
        
        String window = flags.substring(4);
        out[4] = window;
        
        String payload = packet.substring(50);
        
        if(type.equals("00")) {
            
            // If the length is less than max, trim - otherwise use the full payload.
            String content = (decode(length) + 1 < 1024) ? payload.substring(0, decode(length)) : payload;
            
            out[5] = content;
            out[6] = Convert.toText(content);
            
        }
        
        return out;
        
    } // end getContent
    
    public static int getPacketIndexWithPacket(String packet) {
        
        for(int i = packets.size() - 1; i >= 0; i--) {
            String[] pkt_struct = packets.get(i);
            String pkt = pkt_struct[0];
            
            if(packet.equals(pkt)) {
                return i;
            }
        }
        
        return -1;
        
    }
    
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
    private static boolean verify(String bits) {
        
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
    
    /**
     * Builds an acknowledgment packet that can be sent using the node.
     * 
     * @param sequence the sequence number to be acknowledged.
     * @return Returns the full packet, ready to be sent.
     */
    public static String ack(int sequence) {
        
        pack(sequence, 1, "");
        String packet = packets.get(packets.size() - 1)[0];
        
        return packet;
        
    } // end ack
    
    /**
     * Builds a data packet that can be sent using the node.
     * 
     * @param payload the content to be added to the packet, must be in binary.
     * @return Returns the full packet, ready to be sent.
     */
    public static String data(String payload) {
        
        pack(0, payload);
        String packet = packets.get(packets.size() - 1)[0];
        
        return packet;
        
    } // end single
    
} // end class Packet
