package jy.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import jy.main.Main;
import jy.ola.Node;

/**
 *
 * @author jonathanyantz
 */

public class Starter extends JFrame {
    
    private static JLabel alertLabel;
    
    private JTabbedPane tabbedPane;
    
    private JTextField serverDropField;
    private JTextField clientDropField;
    private static JTextField ipServerField;
    private static JTextField portServerField;
    private JTextField ipClientField;
    private JTextField portClientField;
    private JButton serverSubmitButton;
    private JButton clientSubmitButton;

    public Starter() {
        
        createView();
        
        setTitle("Project Mercury");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        
    } // end Starter
    
    private void createView() {
        
        JPanel panel = new JPanel(new BorderLayout());
        getContentPane().add(panel);
        
        alertLabel = new JLabel();
        alertLabel.setPreferredSize(new Dimension(500, 30));
        panel.add(alertLabel, BorderLayout.NORTH);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Server", createServerLayout());
        tabbedPane.addTab("Client", createClientLayout());
        panel.add(tabbedPane, BorderLayout.SOUTH);
        
    } // end createView
    
    private JPanel createServerLayout() {
        
        JPanel ipPanel = new JPanel(new BorderLayout());
        
            JLabel ipDescriptionLabel = new JLabel();
            ipDescriptionLabel.setText("<html><div style=\"width: 28px; margin-left: 5px;\">IP</div></html>");
            ipPanel.add(ipDescriptionLabel, BorderLayout.WEST);

            ipServerField = new JTextField();
            ipServerField.setText("Not set.");
            ipServerField.setEnabled(false);
            ipPanel.add(ipServerField, BorderLayout.CENTER);
        
        JPanel portPanel = new JPanel(new BorderLayout());
        
            JLabel portDescriptionLabel = new JLabel();
            portDescriptionLabel.setText("<html><div style=\"width: 28px; margin-left: 5px;\">Port</div></html>");
            portPanel.add(portDescriptionLabel, BorderLayout.WEST);
            
            portServerField = new JTextField();
            portServerField.setText("Not set.");
            portServerField.setEnabled(false);
            portPanel.add(portServerField, BorderLayout.CENTER);
            
        JPanel infoPanel = new JPanel(new BorderLayout());
        
            infoPanel.add(ipPanel, BorderLayout.NORTH);
            
            infoPanel.add(portPanel, BorderLayout.SOUTH);
            
        JPanel dropPanel = new JPanel(new BorderLayout());
        
            JLabel dropLabel = new JLabel();
            dropLabel.setText("<html><div style=\"margin-left: 5px;\">Please enter the simulated drop rate: </div></html>");
            dropPanel.add(dropLabel, BorderLayout.WEST);

            serverDropField = new JTextField();
            dropPanel.add(serverDropField, BorderLayout.CENTER);
            
        JPanel mainPanel = new JPanel(new BorderLayout());
        
            mainPanel.add(dropPanel, BorderLayout.NORTH);

            serverSubmitButton = new JButton("Start Server");
            serverSubmitButton.addActionListener(new SubmitButtonActionListener());
            mainPanel.add(serverSubmitButton, BorderLayout.SOUTH);
            
            mainPanel.add(infoPanel, BorderLayout.CENTER);
        
        return mainPanel;
        
    } // end createServerLayout
    
    private JPanel createClientLayout() {
        
        JPanel ipPanel = new JPanel(new BorderLayout());
        
            JLabel ipDescriptionLabel = new JLabel();
            ipDescriptionLabel.setText("<html><div style=\"width: 28px; margin-left: 5px;\">IP</div></html>");
            ipPanel.add(ipDescriptionLabel, BorderLayout.WEST);

            ipClientField = new JTextField();
            ipPanel.add(ipClientField, BorderLayout.CENTER);
        
        JPanel portPanel = new JPanel(new BorderLayout());
        
            JLabel portDescriptionLabel = new JLabel();
            portDescriptionLabel.setText("<html><div style=\"width: 28px; margin-left: 5px;\">Port</div></html>");
            portPanel.add(portDescriptionLabel, BorderLayout.WEST);
            
            portClientField = new JTextField();
            portPanel.add(portClientField, BorderLayout.CENTER);
            
        JPanel infoPanel = new JPanel(new BorderLayout());
        
            infoPanel.add(ipPanel, BorderLayout.NORTH);
            
            infoPanel.add(portPanel, BorderLayout.SOUTH);
            
        JPanel dropPanel = new JPanel(new BorderLayout());
        
            JLabel dropLabel = new JLabel();
            dropLabel.setText("<html><div style=\"margin-left: 5px;\">Please enter the simulated drop rate: </div></html>");
            dropPanel.add(dropLabel, BorderLayout.WEST);

            clientDropField = new JTextField();
            dropPanel.add(clientDropField, BorderLayout.CENTER);
            
        JPanel mainPanel = new JPanel(new BorderLayout());
        
            mainPanel.add(dropPanel, BorderLayout.NORTH);

            clientSubmitButton = new JButton("Start Client");
            clientSubmitButton.addActionListener(new SubmitButtonActionListener());
            mainPanel.add(clientSubmitButton, BorderLayout.SOUTH);
            
            mainPanel.add(infoPanel, BorderLayout.CENTER);
        
        return mainPanel;
        
    } // end createClientLayout
    
    public static void updateAlertLabel(String in) {
        
        alertLabel.setText("<html><div style=\"margin: 0 10px;\">" + in + "</div></html>");
        
    } // end updateAlertLabel
    
    public static void setIPServerField(String in) {
        
        ipServerField.setText(in);
        
    } // end setIPServerField
    
    public static void setPortServerField(String in) {
        
        portServerField.setText(in);
        
    } // end setIPServerField
    
    class SubmitButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            // Check if a node has been selected.
            if(tabbedPane.getSelectedIndex() >= 0) {
                
                // Check that the drop field has a value between 0 and 99, inclusive.
                if(clientDropField.getText().matches("^\\d+$") || serverDropField.getText().matches("^\\d+$")) {
                    
                    // Complete checks for server.
                    if(tabbedPane.getSelectedIndex() == 0) {

                        // Check if the drop rate is 0-99.
                        int drop = Integer.parseInt(serverDropField.getText());
                        if(drop >= 0 && drop <= 99) {
                            
                            tabbedPane.setEnabledAt(0, false);
                            tabbedPane.setEnabledAt(1, false);
                            serverDropField.setEnabled(false);
                            serverSubmitButton.setEnabled(false);

                            Main.guiNodeChoice = "s";
                            Main.guiDropChoice = drop;
                            
                            updateAlertLabel("");

                        } else {

                            // Drop rate must be between 0 and 99, inclusive.
                            updateAlertLabel("The drop rate must be a number between 0 and 99, inclusive.");

                        }

                    }

                    // Complete checks for client.
                    if(tabbedPane.getSelectedIndex() == 1) {
                        
                        // Check if the drop rate is 0-99.
                        int drop = Integer.parseInt(clientDropField.getText());
                        if(drop >= 0 && drop <= 99) {

                            tabbedPane.setEnabledAt(0, false);
                            tabbedPane.setEnabledAt(1, false);
                            clientDropField.setEnabled(false);
                            clientSubmitButton.setEnabled(false);
                            
                            ipClientField.setEnabled(false);
                            portClientField.setEnabled(false);

                            Main.guiNodeChoice = "c";
                            Main.guiDropChoice = drop;
                            
                            Node.guiIPChoice = ipClientField.getText();
                            Node.guiPortChoice = portClientField.getText();
                            
                            updateAlertLabel("");
                            
                            SwingUtilities.invokeLater(() -> {
                                new Sender(tabbedPane).setVisible(true);
                            });
                            
                        } else {

                            // Drop rate must be between 0 and 99, inclusive.
                            updateAlertLabel("The drop rate must be a number between 0 and 99, inclusive.");

                        }

                    }
                    
                } else {
                    
                    // Only enter numbers in the drop rate.
                    updateAlertLabel("The drop rate must be a number.");
                    
                }
                
            } else {
                
                // Be sure to pick a node type.
                updateAlertLabel("Be sure to select Server or Client.");

                
            }
            
        } // end actionPerformed
        
    } // end class SubmitButtonActionListener
    
} // end class Starter
