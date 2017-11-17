package jy.main;

import java.io.InputStreamReader;
import java.util.Scanner;

import jy.ola.*;
import jy.tools.*;

/**
 *
 * @author jonathanyantz
 */

public class Main {

    public static void main(String[] args) {
        
        //test();
                
        boolean nodeCont = true;
        int node = 0;
        
        while(nodeCont) {
            Scanner input = new Scanner(new InputStreamReader(System.in));
            
            System.out.print("Server[s] / Client[c]: ");
            String choice = input.nextLine();
            
            if(choice.equalsIgnoreCase("s")) {
                node = 0;
                nodeCont = false;
            }
            
            if(choice.equalsIgnoreCase("c")) {
                node = 1;
                nodeCont = false;
            }
        }
        
        boolean dropCont = true;
        int drop = 0;
        
        while(dropCont) {
            Scanner input = new Scanner(new InputStreamReader(System.in));
            
            System.out.print("Drop Rate[0-99]: ");
            drop = input.nextInt();
            
            if(drop >= 0 && drop <= 99) {
                dropCont = false;
            }
        }
        
        Node n = new Node(node, drop);
        
    } // end main
    
    private static void test() {
        
        String s = Convert.toBinary(File.readAll("test-content-provided.txt"));
        System.out.println("Binary: " + s);
        System.out.println("Length: " + s.length());
        System.out.println("% 8: " + s.length() % 8);
        System.out.println("Packets: " + Math.ceil(s.length() / 1024.0));
        System.out.println("Full: " + s.length() / 1024);
        System.out.println("1 Partial - bits: " + ((s.length() / 1024.0) - (s.length() / 1024)) * 1024);
        
    } // end test
    
} // end class Main
