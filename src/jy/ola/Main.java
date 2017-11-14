package jy.ola;

import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *
 * @author jonathanyantz
 */

public class Main {

    public static void main(String[] args) {
        
        boolean cont = true;
        
        while(cont) {
            Scanner input = new Scanner(new InputStreamReader(System.in));
            System.out.print("Server[s] / Client[c]: ");
            String choice = input.nextLine();
            
            if(choice.equalsIgnoreCase("s")) {
                Node n = new Node(0);
                cont = false;
            }
            
            if(choice.equalsIgnoreCase("c")) {
                Node n = new Node(1);
                cont = false;
            }
        }
        
    } // end main
    
} // end class Main
