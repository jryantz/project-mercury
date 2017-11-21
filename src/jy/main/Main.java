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
        
        while(mainCont) {
            Scanner input = new Scanner(new InputStreamReader(System.in));
            
            System.out.print("> ");
            Command.run(input.nextLine());
        }
        
    } // end main
    
    private static void test() {
        
        Packet.packAll(0, "test-content-provided.txt");
        
        for(int i = 0; i < Packet.packets.size(); i++) {
            System.out.println("[" + i + " - " + Packet.packets.get(i).length() + "]: " + Packet.packets.get(i));
        }
        
    } // end test
    
} // end class Main
