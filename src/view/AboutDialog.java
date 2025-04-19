package view;

import javax.swing.*;
import java.awt.*;

public class AboutDialog extends JDialog {
  public AboutDialog(JFrame parent) {
    super(parent, "About Kill Doctor Lucky", true);
    setLayout(new BorderLayout(10, 10));
    add(new JLabel("<html><center>" + "<h2>Kill Doctor Lucky</h2>" + "By Your Name<br/>"
        + "Background image by <i>SpookyBG.com</i>" + "</center></html>"), BorderLayout.CENTER);

    JButton ok = new JButton("OK");
    ok.addActionListener(e -> dispose());
    JPanel p = new JPanel();
    p.add(ok);
    add(p, BorderLayout.SOUTH);

    pack();
    setResizable(false);
    setLocationRelativeTo(parent);
  }
}
