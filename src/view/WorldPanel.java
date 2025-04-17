package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

/**
 * A simple JPanel that just paints the current BufferedImage.
 */
public class WorldPanel extends JPanel {
  private BufferedImage image;

  public WorldPanel() {
    setPreferredSize(new Dimension(600, 400));
  }

  /** Called by GameView whenever the map changes. */
  public void setImage(BufferedImage img) {
    this.image = img;
    if (img != null) {
      setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
      revalidate();
      repaint();
    }
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (image != null) {
      g.drawImage(image, 0, 0, this);
    }
  }
}
