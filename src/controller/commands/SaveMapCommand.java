package controller.commands;

import killdoctorlucky.model.Iworld;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import controller.Icommand;

/**
 * Command to generate and save the world map as a PNG file.
 */
public class SaveMapCommand implements Icommand {
  private final String fileName;

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
