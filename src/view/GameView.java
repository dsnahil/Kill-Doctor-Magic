// File: src/view/GameView.java
package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Objects;

public class GameView extends JFrame implements IView {
  private final JLabel mapLabel = new JLabel();
  private final JTextArea logArea = new JTextArea();
  private IViewFeatures features;
  private Image bgImage;

  public GameView() throws Exception {
    super("Kill Doctor Lucky");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setSize(1000, 700);
    setLayout(new BorderLayout());
    // background
    bgImage = new ImageIcon("res/spooky_bg.jpeg").getImage();
    ((JComponent) getContentPane()).setOpaque(false);
    setContentPane(new JPanel() {
      public void paintComponent(Graphics g) {
        g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
      }
    });
    initUI();
  }

  public void loadSpookyFont(String path) {
    try (InputStream in = getClass().getResourceAsStream(path)) {
      Font f = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(18f);
      UIManager.put("Button.font", f);
      UIManager.put("TextArea.font", f);
    } catch (Exception ignored) {
    }
  }

  private void style(JButton b) {
    b.setOpaque(false);
    b.setContentAreaFilled(false);
    b.setForeground(Color.WHITE);
    b.setBorder(BorderFactory.createLineBorder(new Color(200, 50, 50), 2));
  }

  private void initUI() {
    JPanel west = new JPanel(new GridLayout(8, 1, 5, 5));
    west.setOpaque(false);
    String[] names = { "Next Turn", "Move", "Pickup", "Look", "Attack", "Describe", "Save Map",
        "Move Pet" };
    for (String n : names) {
      JButton b = new JButton(n);
      style(b);
      b.addActionListener(e -> {
        switch (n) {
          case "Next Turn":
            features.handleNextTurn();
            break;
          case "Move":
            features.handleMove();
            break;
          case "Pickup":
            features.handlePickup();
            break;
          case "Look":
            features.handleLook();
            break;
          case "Attack":
            features.handleAttack();
            break;
          case "Describe":
            features.handleDescribe();
            break;
          case "Save Map":
            features.handleSaveMap();
            break;
          case "Move Pet":
            features.handleMovePet();
            break;
        }
      });
      west.add(b);
    }
    add(west, BorderLayout.EAST);

    mapLabel.setPreferredSize(new Dimension(600, 600));
    add(new JScrollPane(mapLabel), BorderLayout.CENTER);

    logArea.setEditable(false);
    logArea.setRows(5);
    add(new JScrollPane(logArea), BorderLayout.SOUTH);
  }

  @Override
  public void redrawMap(BufferedImage map) {
    mapLabel.setIcon(new ImageIcon(map));
  }

  @Override
  public void appendToLog(String text) {
    logArea.append(text + "\n");
    logArea.setCaretPosition(logArea.getDocument().getLength());
  }

  @Override
  public void setViewFeatures(IViewFeatures f) {
    this.features = f;
  }
}
