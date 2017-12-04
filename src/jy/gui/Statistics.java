package jy.gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author jonathanyantz
 */

public class Statistics extends JFrame {
    
    private static JLabel execDataLabel;
    private static JLabel totalDataLabel;
    private static JLabel successfulDataLabel;
    private static JLabel failedDataLabel;
    private static JLabel genDataLabel;
    private static JLabel dropDataLabel;

    public Statistics() {
        
        createView();
        
        setTitle("Project Mercury");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        
    } // end Statistics
    
    public static void setData(String edl, String tdl, String sdl, String fdl, String gdl, String ddl) {
        
        execDataLabel.setText("<html><div style=\"width: 80px; margin-top: 10px;\">" + edl + "</div></html>");
        totalDataLabel.setText("<html><div style=\"width: 80px;\">" + tdl + "</div></html>");
        successfulDataLabel.setText("<html><div style=\"width: 80px;\">" + sdl + "</div></html>");
        failedDataLabel.setText("<html><div style=\"width: 80px;\">" + fdl + "</div></html>");
        genDataLabel.setText("<html><div style=\"width: 80px;\">" + gdl + "</div></html>");
        dropDataLabel.setText("<html><div style=\"width: 80px; margin-bottom: 10px;\">" + ddl + "</div></html>");
        
    } // end setExecDataLabel
    
    private void createView() {
        
        JPanel sentPacketsPane = new JPanel(new BorderLayout());
        
            JPanel totalPacketsPane = new JPanel(new BorderLayout());
            
                JLabel totalInfoLabel = new JLabel();
                totalInfoLabel.setText("<html><div style=\"width: 150px; margin-left: 5px;\">Packets Sent (Total): </div></html>");
                totalPacketsPane.add(totalInfoLabel, BorderLayout.WEST);
                
                totalDataLabel = new JLabel();
                totalDataLabel.setText("<html><div style=\"width: 80px;\"></div></html>");
                totalPacketsPane.add(totalDataLabel, BorderLayout.CENTER);
            
            JPanel successfulPacketsPane = new JPanel(new BorderLayout());
            
                JLabel successfulInfoLabel = new JLabel();
                successfulInfoLabel.setText("<html><div style=\"width: 150px; margin-left: 5px;\">Packets Sent (Successful): </div></html>");
                successfulPacketsPane.add(successfulInfoLabel, BorderLayout.WEST);
                
                successfulDataLabel = new JLabel();
                successfulDataLabel.setText("<html><div style=\"width: 80px;\"></div></html>");
                successfulPacketsPane.add(successfulDataLabel, BorderLayout.CENTER);
            
            JPanel failedPacketsPane = new JPanel(new BorderLayout());
            
                JLabel failedInfoLabel = new JLabel();
                failedInfoLabel.setText("<html><div style=\"width: 150px; margin-left: 5px;\">Packets Sent (Fail): </div></html>");
                failedPacketsPane.add(failedInfoLabel, BorderLayout.WEST);
                
                failedDataLabel = new JLabel();
                failedDataLabel.setText("<html><div style=\"width: 80px;\"></div></html>");
                failedPacketsPane.add(failedDataLabel, BorderLayout.CENTER);
                
            sentPacketsPane.add(totalPacketsPane, BorderLayout.NORTH);
            sentPacketsPane.add(successfulPacketsPane, BorderLayout.CENTER);
            sentPacketsPane.add(failedPacketsPane, BorderLayout.SOUTH);
            
        JPanel packetsPane = new JPanel(new BorderLayout());
        
            JPanel genPacketsPane = new JPanel(new BorderLayout());
            
                JLabel genInfoLabel = new JLabel();
                genInfoLabel.setText("<html><div style=\"width: 150px; margin-left: 5px;\">Generated Packets: </div></html>");
                genPacketsPane.add(genInfoLabel, BorderLayout.WEST);
                
                genDataLabel = new JLabel();
                genDataLabel.setText("<html><div style=\"width: 80px;\"></div></html>");
                genPacketsPane.add(genDataLabel, BorderLayout.CENTER);
                
            packetsPane.add(genPacketsPane, BorderLayout.NORTH);
            packetsPane.add(sentPacketsPane, BorderLayout.CENTER);
            
        JPanel panel = new JPanel(new BorderLayout());
        
            JPanel execTimePane = new JPanel(new BorderLayout());
            
                JLabel execInfoLabel = new JLabel();
                execInfoLabel.setText("<html><div style=\"width: 150px; margin: 10px 0 0 5px;\">Execution Time: </div></html>");
                execTimePane.add(execInfoLabel, BorderLayout.WEST);
                
                execDataLabel = new JLabel();
                execDataLabel.setText("<html><div style=\"width: 80px;\"></div></html>");
                execTimePane.add(execDataLabel, BorderLayout.CENTER);
                
            JPanel dropRatePane = new JPanel(new BorderLayout());
            
                JLabel dropInfoLabel = new JLabel();
                dropInfoLabel.setText("<html><div style=\"width: 150px; margin: 0 0 10px 5px;\">Drop Rate: </div></html>");
                dropRatePane.add(dropInfoLabel, BorderLayout.WEST);
                
                dropDataLabel = new JLabel();
                dropDataLabel.setText("<html><div style=\"width: 80px;\"></div></html>");
                dropRatePane.add(dropDataLabel, BorderLayout.CENTER);
                
            panel.add(execTimePane, BorderLayout.NORTH);
            panel.add(packetsPane, BorderLayout.CENTER);
            panel.add(dropRatePane, BorderLayout.SOUTH);
        
        getContentPane().add(panel);
        
    } // end createView()
    
} // end class Statistics
