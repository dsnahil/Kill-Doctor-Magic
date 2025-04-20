package view;

import java.awt.image.BufferedImage;

/**
 * Draws the world map image, appends to the game log, and registers controller
 * callbacks.
 */
public interface Iview {

  /**
   * Draws the world map image.
   *
   * @param map the BufferedImage of the world map.
   */
  void redrawMap(BufferedImage map);

  /**
   * Appends a line to the game log.
   *
   * @param text the text to append.
   */
  void appendToLog(String text);

  /**
   * Registers controller callbacks.
   *
   * @param features the IViewFeatures implementation.
   */
  void setViewFeatures(IviewFeatures features);
}
