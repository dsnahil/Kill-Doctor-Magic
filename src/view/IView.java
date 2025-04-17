// File: src/view/IView.java
package view;

import java.awt.image.BufferedImage;

public interface IView {
  /** draw the world map image **/
  void redrawMap(BufferedImage map);

  /** append a line to the game log **/
  void appendToLog(String text);

  /** register controller callbacks **/
  void setViewFeatures(IViewFeatures f);
}
