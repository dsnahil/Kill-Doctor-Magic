package controller;

import java.io.IOException;

/**
 * Represents a controller for the Kill Doctor Lucky game.
 */
public interface Icontroller {
  /**
   * Starts the game.
   *
   * @throws IOException if output transmission fails.
   */
  void startGame() throws IOException;
}
