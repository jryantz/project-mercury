package jy.tools;

import java.math.BigInteger;

/**
 *
 * @author jonathanyantz
 */

public class Convert {
    
    public static String toBinary(byte[] bytes) {
        
        StringBuilder binary = new StringBuilder();
        
        for (byte b : bytes) {
            int val = b;
            
            for(int i = 0; i < 8; i++) {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        
        return binary.toString();
        
    } // end toBinary
    
    public static String toText(String bit) {
        
        return new String(new BigInteger(bit, 2).toByteArray());
        
    } // end toText
    
} // end class Convert
