package jy.tools;

import java.math.BigInteger;

/**
 *
 * @author jonathanyantz
 */

public class Convert {
    
    /**
     * Converts a byte array to 8 bit binary.
     * 
     * @param bytes the byte array input, to be converted.
     * @return Returns a string of bits that represent the inputted byte array.
     */
    public static String toBinary(byte[] bytes) {
        
        StringBuilder binary = new StringBuilder();
        
        for(byte b : bytes) {
            int val = b;
            
            for(int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        
        return binary.toString();
        
    } // end toBinary
    
    /**
     * Converts a binary string to a text string.
     * 
     * @param bit the string of bits, usually outputted by the toBinary method.
     * @return Returns the text string.
     */
    public static String toText(String bit) {
        
        return new String(new BigInteger(bit, 2).toByteArray());
        
    } // end toText
    
} // end class Convert
