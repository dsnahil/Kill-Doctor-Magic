// File: src/controller/Driver.java
package controller;

import killdoctorlucky.model.Iworld;
import killdoctorlucky.model.World;
import util.RandomGenerator;
import view.GameView;
import view.IViewFeatures;

import javax.swing.*;
import java.io.IOException;

public class Driver {
  public static void main(String[] args) {
    // Use system look-and-feel so we don't need an external FlatLaf dependency
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ignored) {
    }

    // Load world
    String path = args.length > 0 ? args[0] : "res/mansion.txt";
    Iworld world;
    try {
      world = new World(path);
    } catch (IOException e) {
      System.err.println("Failed to load world: " + e.getMessage());
      return;
    }

    // Launch GUI
    SwingUtilities.invokeLater(() -> {
      try {
        GameView view = new GameView();
        GuiController ctrl = new GuiController(world, view, 20);
        // GuiController implements IViewFeatures
        view.setViewFeatures((IViewFeatures) ctrl);
        view.setVisible(true);
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
  }
}
