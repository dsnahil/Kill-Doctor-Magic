package controller;

import killdoctorlucky.model.Iworld;
import killdoctorlucky.model.World;
import view.GameView;

import javax.swing.*;
import java.io.IOException;

public class Driver {
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ignore) {
    }

    String path = args.length > 0 ? args[0] : "res/mansion.txt";
    Iworld world;
    try {
      world = new World(path);
    } catch (IOException ex) {
      System.err.println("Failed to load world: " + ex.getMessage());
      return;
    }

    SwingUtilities.invokeLater(() -> {
      try {
        GameView view = new GameView();
        new GuiController(world, view, 20);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(1);
      }
    });
  }
}
