package view;

import javax.swing.*;

public class AboutDialog extends JDialog {
  public AboutDialog(JFrame parent) {
    super(parent, "About Kill Doctor Lucky", true);
    add(new JLabel("<html><center>Kill Doctor Lucky<br/>By Your Name</center></html>"),
        SwingConstants.CENTER);
    setSize(240, 120);
    setLocationRelativeTo(parent);
  }
}
