package jy.main;

import java.io.InputStreamReader;
import java.util.Scanner;

import jy.ola.Node;
import jy.ola.Packet;

/**
 *
 * @author jonathanyantz
 */

public class Main {
    
    public static Node node;

    /**
     * The main method for the beginning of execution.
     * 
     * @param args any arguments that need to be supplied to start the program.
     */
    public static void main(String[] args) {
        
        //test();
                
        boolean typeCont = true;
        int type = 0;
        
        while(typeCont) {
            Scanner input = new Scanner(new InputStreamReader(System.in));
            
            System.out.print("Server[s] / Client[c]: ");
            String choice = input.nextLine();
            
            if(choice.equalsIgnoreCase("s")) {
                type = 0;
                typeCont = false;
            }
            
            if(choice.equalsIgnoreCase("c")) {
                type = 1;
                typeCont = false;
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
        
        node = new Node(type, drop);
        
        try {
            Thread.sleep(300);
        } catch(InterruptedException e) {}
        
        boolean mainCont = true;
        
        Command.run("commands");
        
        while(mainCont) {
            Scanner input = new Scanner(new InputStreamReader(System.in));
            
            Command.run(input.nextLine());
        }
        
    } // end main
    
    /**
     * A test method for trying different use-cases.
     */
    private static void test() {
        
        Packet.packAll("test-content-provided.txt");
        
//        for(int i = 0; i < Packet.packets.size(); i++) {
//            System.out.println("[" + i + " - " + Packet.packets.get(i)[0].length() + "]: " + Packet.packets.get(i)[0]);
//        }
        
    } // end test
    
} // end class Main
