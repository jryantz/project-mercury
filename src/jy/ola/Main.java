package jy.ola;

import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *
 * @author jonathanyantz
 */

public class Main {

    public static void main(String[] args) {
        
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
        
        boolean nodeCont = true;
        String choice;
        
        while(nodeCont) {
            Scanner input = new Scanner(new InputStreamReader(System.in));
            
            System.out.print("Server[s] / Client[c]: ");
            choice = input.nextLine();
            
            if(choice.equalsIgnoreCase("s")) {
                Node n = new Node(0, drop);
                nodeCont = false;
            }
            
            if(choice.equalsIgnoreCase("c")) {
                Node n = new Node(1, drop);
                nodeCont = false;
            }
        }
        
    } // end main
    
} // end class Main
