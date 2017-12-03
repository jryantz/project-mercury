package jy.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jonathanyantz
 */

public class File {
    
    /**
     * Reads an entire file to its byte representation.
     * 
     * @param location the location of the file to be read.
     * @return Returns the byte array of the file.
     */
    public static byte[] readAll(String location) {
        
        Path path = Paths.get(location);
        
        byte[] data = new byte[1024];
        
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return data;
        
    } // end readAll
    
    /**
     * Creates and writes an entire file from a byte array.
     * 
     * @param location The location to where the file should be written to.
     * @param content The byte array of content to be written.
     */
    public static void writeAll(String location, byte[] content) {
        
        Path path = Paths.get(location);
        
        try {
            Files.write(path, content);
        } catch (IOException e) {
            Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, e);
        }
        
        System.out.println("\n*** File Received and Written ***");
        
    } // end writeAll
    
    /**
     * Lists all files inside of the directory that the JAR file is in.
     * 
     * @return Returns an array of all of the files.
     */
    public static String[] list() {
        
        java.io.File folder = new java.io.File(".");
        java.io.File[] list = folder.listFiles();
        
        String[] out = new String[list.length];
        
        for(int i = 0; i < list.length; i++) {
            if(list[i].isFile()) {
                out[i] = list[i].getName();
            } else if(list[i].isDirectory()) {
                out[i] = "/" + list[i].getName();
            }
        }
        
        Arrays.sort(out);
        
        return out;
        
    } // end list
    
    /**
     * Converts array list content to byte array content for file writing.
     * 
     * @param content
     * @return 
     */
    public static byte[] convert(ArrayList<String[]> content) {
        
        String binary = "";
        
        for(int i = 0; i < content.size(); i++) {
            for(int j = 0; j < content.size(); j++) {
                if(content.get(j)[0].equals(i + "")) {
                    binary += content.get(j)[1];
                    break;
                }
            }
        }
        
        return Convert.toText(binary).getBytes();
        
    } // end convert
    
} // end class File
