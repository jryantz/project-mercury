package jy.main;

import java.io.InputStreamReader;
import java.util.Scanner;
import javax.swing.SwingUtilities;
import jy.gui.Starter;
import jy.ola.Node;
import jy.ola.Packet;

/**
 *
 * @author jonathanyantz
 */

public class Main {
    
    public static Node node;
    
    public static String guiNodeChoice = "";
    public static int guiDropChoice = -1;

    /**
     * The main method for the beginning of execution.
     * 
     * @param args any arguments that need to be supplied to start the program.
     */
    public static void main(String[] args) {
        
        SwingUtilities.invokeLater(() -> {
            new Starter().setVisible(true);
        });
                
        boolean typeCont = true;
        int type = 0;
        
        while(typeCont) {
            String choice = guiNodeChoice;
            
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
            int choice = guiDropChoice;
            
            if(choice >= 0 && choice <= 99) {
                drop = choice;
                dropCont = false;
            }
        }
        
        node = new Node(type, drop);
        
    } // end main
    
} // end class Main
