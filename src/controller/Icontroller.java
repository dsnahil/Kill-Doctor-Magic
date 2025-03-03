package controller;

import java.io.IOException;

/**
 * Represents a controller for the Kill Doctor Lucky game. The controller is
 * responsible for handling user input/output and coordinating actions on the
 * model.
 */
public interface Icontroller {

  /**
   * Starts or plays the game until it ends or the user quits.
   *
   * @throws IOException if transmission of output fails
   */
  void startGame() throws IOException;
}