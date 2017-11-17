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
    
    public static void writeAll(String location, byte[] content) {
        
        Path path = Paths.get(location);
        
        try {
            Files.write(path, content);
        } catch (IOException e) {
            Logger.getLogger(File.class.getName()).log(Level.SEVERE, null, e);
        }
        
    } // end writeAll
    
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
