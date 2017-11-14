package jy.ola;

import java.io.InputStreamReader;
import java.util.Scanner;

/**
 *
 * @author jonathanyantz
 */

public class Main {
    
    public static boolean server = false;
    public static boolean client = false;

    public static void main(String[] args) {
        
        boolean cont = true;
        
        while(cont) {
            Scanner input = new Scanner(new InputStreamReader(System.in));
            System.out.print("Server[s] / Client[c]: ");
            String choice = input.nextLine();
            
            if(choice.equalsIgnoreCase("s")) {
                Server s = new Server();
                server = true;
                client = false;
                cont = false;
            }
                
            if(choice.equalsIgnoreCase("c")) {
                Client c = new Client();
                server = false;
                client = true;
                cont = false;
            }
        }
        
    } // end main
    
} // end class Main
