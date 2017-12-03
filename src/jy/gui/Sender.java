package jy.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import jy.main.Command;
import jy.ola.Packet;
import jy.tools.File;
import jy.tools.ListDialog;

/**
 *
 * @author jonathanyantz
 */

public class Sender extends JFrame {
    
    private final JFrame frame = this;
    private final String[] list = File.list();
    private JLabel selectedFileLabel;
    private JButton selectButton;
    private JToggleButton GBNButton;
    private JToggleButton SAWButton;
    private JButton sendButton;

    public Sender(Component locationComp) {
        
        createView();
        
        setTitle("Project Mercury");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pack();
        setLocationRelativeTo(locationComp);
        setResizable(false);
        
    } // end Sender
    
    private void createView() {
        
        JPanel filePane = new JPanel();
        filePane.setLayout(new BoxLayout(filePane, BoxLayout.PAGE_AXIS));
        filePane.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
            JLabel infoLabel = new JLabel();
            infoLabel.setText("Please select a file to send.");
            infoLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            filePane.add(infoLabel);
            
            filePane.add(Box.createVerticalStrut(5));

            selectedFileLabel = new JLabel();
            selectedFileLabel.setText("Nothing selected.");
            selectedFileLabel.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            filePane.add(selectedFileLabel);

            selectButton = new JButton("File Selector");
            selectButton.setAlignmentX(JComponent.CENTER_ALIGNMENT);
            selectButton.addActionListener((ActionEvent e) -> {
                String selectedName = ListDialog.showDialog(frame, selectButton, "Select a file to send.", "File Selector", list, selectedFileLabel.getText(), null);
                selectedFileLabel.setText(selectedName);
            });
            
            filePane.add(Box.createRigidArea(new Dimension(150, 10)));
            
            filePane.add(selectButton);
            
        JPanel protocolPane = new JPanel(new BorderLayout());
        
            JLabel protocolLabel = new JLabel();
            protocolLabel.setText("<html><div style=\"margin-left: 5px;\">Please select a protocol.</div></html>");
            protocolPane.add(protocolLabel, BorderLayout.NORTH);
        
            GBNButton = new JToggleButton("GBN");
            GBNButton.addActionListener((ActionEvent e) -> {
                if(SAWButton.isSelected()) {
                    SAWButton.setSelected(false);
                }
                
                Packet.protocol = 0;
            });
            protocolPane.add(GBNButton, BorderLayout.WEST);
        
            SAWButton = new JToggleButton("SAW");
            SAWButton.addActionListener((ActionEvent e) -> {
                if(GBNButton.isSelected()) {
                    GBNButton.setSelected(false);
                }
                
                Packet.protocol = 1;
            });
            protocolPane.add(SAWButton, BorderLayout.EAST);
            
        JPanel panel = new JPanel(new BorderLayout());
            
            panel.add(protocolPane, BorderLayout.NORTH);
            
            panel.add(filePane, BorderLayout.CENTER);
            
            sendButton = new JButton("Send File");
            sendButton.addActionListener(new SendButtonActionListener());
            panel.add(sendButton, BorderLayout.SOUTH);
        
        getContentPane().add(panel);
        
    } // end createView
    
    class SendButtonActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            if(GBNButton.isSelected() || SAWButton.isSelected()) {
            
                String[] check = File.list();
                String file = "10000000";
                for(int i = 0; i < check.length; i++) {
                    if(selectedFileLabel.getText().equals(check[i])) {
                        file = i + "";
                        break;
                    }
                }

                if(Integer.parseInt(file) < check.length && Integer.parseInt(file) >= 0) {
                    
                    GBNButton.setEnabled(false);
                    SAWButton.setEnabled(false);
                    selectButton.setEnabled(false);
                    sendButton.setEnabled(false);
                    
                    Command.run("file-send " + file);
                    
                    sendButton.setText("File Sent");
                    
                } else {
                    Starter.updateAlertLabel("File does not exist.");
                }
            
            } else {
                
                Starter.updateAlertLabel("A protocol must be selected.");
                
            }
            
        }
        
    } // end class SendButtonActionListener
    
} // end class Sender
