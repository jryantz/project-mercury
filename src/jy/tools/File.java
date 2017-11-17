package jy.tools;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

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
    
} // end class File
