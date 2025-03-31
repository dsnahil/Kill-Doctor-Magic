package controller.commands;

import controller.Icommand;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import killdoctorlucky.model.Iworld;

/**
 * Command to generate and save the world map as a PNG file.
 */
public class SaveMapCommand implements Icommand {
  private final String fileName;

  /**
   * Command to generate and save the world map as a PNG file.
   */
  public SaveMapCommand(String fileName) {
    this.fileName = fileName;
  }

  @Override
  public void execute(Iworld model) {
    BufferedImage map = model.generateWorldMap();
    try {
      ImageIO.write(map, "png", new File(fileName));
      System.out.println("Map saved as " + fileName);
    } catch (IOException e) {
      System.out.println("Failed to save map: " + e.getMessage());
    }
  }
}
