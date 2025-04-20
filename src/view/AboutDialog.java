package view;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

/**
 * Dialog showing information about the Kill Doctor Lucky game.
 */
public class AboutDialog extends JDialog {
  private static final long serialVersionUID = 1L;

  /**
   * Creates a new About dialog.
   *
   * @param parent the parent frame for this dialog
   */
  public AboutDialog(final JFrame parent) {
    super(parent, "About Kill Doctor Lucky", true);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    initComponents();
    pack();
    setLocationRelativeTo(parent);
  }

  /**
   * Builds and lays out all components in the dialog.
   */
  private void initComponents() {
    JPanel content = new JPanel();
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
    content.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "About",
        TitledBorder.CENTER, TitledBorder.TOP));

    JLabel title = new JLabel("Kill Doctor Lucky", SwingConstants.CENTER);
    JLabel version = new JLabel("Version 1.0", SwingConstants.CENTER);
    JLabel author = new JLabel("Author: Your Name", SwingConstants.CENTER);

    content.add(title);
    content.add(version);
    content.add(author);

    add(content);
  }
}
