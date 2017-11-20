package jy.tools;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jonathanyantz
 */

public class File {
    
    /**
     * Reads a file, into memory, line by line, for later manipulation.
     * 
     * @param location the location of the file that should be read in.
     * @return Returns an arraylist containing all of the lines of the file.
     */
    public static ArrayList<String> read(String location) {
        
        ArrayList<String> content = new ArrayList();
        
        try {
            
            Scanner reader = new Scanner(new java.io.File(location));
            
            while(reader.hasNextLine()) {
                String line = reader.nextLine();
                content.add(line + "\n");
            }
            
        } catch(FileNotFoundException e) {
            System.out.println("File does not exist.");
            content.clear();
            
            return new ArrayList();
        }
        
        return content;
        
    } // end read
    
    /**
     * Creates a file with a given name and fills the file with the given content.
     * 
     * @param name the desired name of the file.
     * @param content the content to be put into the file.
     */
    public static void write(String name, ArrayList<String> content) {
        
        String output = "";
        
        for(int i = 0; i < content.size(); i++) {
            
            output += content.get(i);
            
        }
        
        try {

            java.io.File file = new java.io.File(name);

            if(!file.exists()) {
                file.createNewFile();
            }
            
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            try (BufferedWriter bw = new BufferedWriter(fw)) {
                bw.write(output);
            }
            
        } catch(IOException e) {}
        
    } // end write
    
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
    
} // end class File
