package controller;

import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import killdoctorlucky.model.Iworld;
import killdoctorlucky.model.World;
import view.GameView;

/**
 * Driver class to launch the Kill Doctor Lucky GUI.
 */
public class Driver {

  /**
   * Entry point for the Kill Doctor Lucky application.
   *
   * @param args command-line arguments specifying world file path
   */
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ignore) { // ignore and continue with default L&F
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

        // --- SHOW THE GAME INSTRUCTIONS DIALOG ---
        String instructions = "<html>" + "<h2>Welcome to Kill Doctor Lucky!</h2>" + "<ul>"
            + "<li>Each turn, you may <b>Move</b>, <b>Pickup</b>, or "
            + "<b>Attack</b> if you are in the same room as Doctor Lucky.</li>"
            + "<li>To attack successfully, Doctor Lucky must be in your room <em>and</em> "
            + "unobserved by any other player <em>and</em> the pet.</li>"
            + "<li>If the pet is in that room, it alerts Doctor Lucky and your attack fails.</li>"
            + "<li>Click on the map or on the highlighted legal "
            + "rooms to move—no more typing names twice!</li>"
            + "</ul>" + "</html>";
        JOptionPane.showMessageDialog(view, instructions, "How to Play",
            JOptionPane.INFORMATION_MESSAGE);

        // 2) Ask how many human players
        String humanCountInput = JOptionPane.showInputDialog(view, "How many human players?",
            "Players Setup", JOptionPane.QUESTION_MESSAGE);
        int numHuman = Integer.parseInt(humanCountInput.trim());

        // 3) Ask for their names, comma‑separated
        String names = JOptionPane.showInputDialog(view,
            "Enter " + numHuman + " human player name(s), comma‑separated:", "Players Setup",
            JOptionPane.QUESTION_MESSAGE);
        String[] humanNames = names.split("\\s*,\\s*");
        for (int i = 0; i < numHuman && i < humanNames.length; i++) {
          world.addPlayer(humanNames[i].trim(), 0);
          view.appendToLog("Added human player: " + humanNames[i].trim());
        }

        // 4) Ask how many computer players
        String aiCountInput = JOptionPane.showInputDialog(view, "How many computer players?",
            "Players Setup", JOptionPane.QUESTION_MESSAGE);
        int numAi = Integer.parseInt(aiCountInput.trim());
        for (int i = 1; i <= numAi; i++) {
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
