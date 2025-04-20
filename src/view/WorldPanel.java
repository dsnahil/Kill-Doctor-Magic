package view;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * A simple JPanel that just paints the current BufferedImage.
 */
public class WorldPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  private BufferedImage image;

  /**
   * Constructs a new WorldPanel with default preferred size.
   */
  public WorldPanel() {
    setPreferredSize(new Dimension(600, 400));
  }

  /**
   * Updates the panel's image and resizes to match.
   *
   * @param img the new image to display, may be null.
   */
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
