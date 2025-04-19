// File: src/controller/Driver.java
package controller;

import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import killdoctorlucky.model.Iworld;
import killdoctorlucky.model.World;
import view.GameView;

public class Driver {

  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ignore) {
    }

    // which file to load?
    String path = (args.length > 0) ? args[0] : "res/mansion.txt";
    final Iworld world;
    try {
      world = new World(path);
    } catch (IOException ex) {
      System.err.println("Failed to load world: " + ex.getMessage());
      return;
    }

    SwingUtilities.invokeLater(() -> {
      try {
        // 1) Build the Swing view
        GameView view = new GameView();

        // 2) Ask how many human players
        String hCount = JOptionPane.showInputDialog(view, "How many human players?",
            "Players Setup", JOptionPane.QUESTION_MESSAGE);
        int numHuman = Integer.parseInt(hCount.trim());

        // 3) Ask for their names, comma‑separated
        String names = JOptionPane.showInputDialog(view,
            "Enter " + numHuman + " human player names, comma‑separated:", "Players Setup",
            JOptionPane.QUESTION_MESSAGE);
        String[] humanNames = names.split("\\s*,\\s*");
        for (int i = 0; i < numHuman && i < humanNames.length; i++) {
          world.addPlayer(humanNames[i].trim(), 0);
          view.appendToLog("Added human player: " + humanNames[i].trim());
        }

        // 4) Ask how many computer players
        String aCount = JOptionPane.showInputDialog(view, "How many computer players?",
            "Players Setup", JOptionPane.QUESTION_MESSAGE);
        int numAI = Integer.parseInt(aCount.trim());
        for (int i = 1; i <= numAI; i++) {
          String aiName = "CPU" + i;
          world.addPlayer(aiName, 0);
          view.appendToLog("Added AI player: " + aiName);
        }

        // 5) Now start the GUI controller
        new GuiController(world, view, 20);

        // 6) Finally, show the window
        view.setVisible(true);

      } catch (Exception ex) {
        ex.printStackTrace();
        System.exit(1);
      }
    });
  }
}
