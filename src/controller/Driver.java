package controller;

import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import killdoctorlucky.model.ComputerPlayer;
import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Iworld;
import killdoctorlucky.model.World;
import util.RandomGenerator;
import view.GameView;

/**
 * Driver class to launch the Kill Doctor Lucky GUI. Usage: java
 * controller.Driver [world‑file] [maxTurns]
 */
public class Driver {

  /**
   * Entry point for the Kill Doctor Lucky application.
   *
   * @param args command-line arguments specifying: args[0] = path to world file
   *             (optional, defaults to "res/mansion.txt") args[1] = max number of
   *             turns (optional, defaults to 20)
   */
  public static void main(String[] args) {
    // 1) Try to pick up the system look‑and‑feel—but catch *only* its declared
    // exceptions.
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | UnsupportedLookAndFeelException ignore) {
      // Intentionally ignoring UI look & feel setup failures
    }

    // 2) Determine world file
    String path = (args.length > 0) ? args[0] : "res/mansion.txt";

    // 3) Parse maxTurns if provided
    int maxTurns = 20;
    if (args.length > 1) {
      try {
        maxTurns = Integer.parseInt(args[1]);
      } catch (NumberFormatException nfe) {
        System.err.println("Invalid maxTurns '" + args[1] + "', using default 20.");
      }
    }
    final int finalMaxTurns = maxTurns;

    // 4) Load the world (only IOException can be thrown here)
    final Iworld world;
    try {
      world = new World(path);
    } catch (IOException ioe) {
      System.err.println("Failed to load world: " + ioe.getMessage());
      return;
    }

    // 5) Launch the GUI on the EDT
    SwingUtilities.invokeLater(() -> {
      try {
        GameView view = new GameView();

        // show instructions dialog
        String instructions = "<html>" + "<h2>Welcome to Kill Doctor Lucky!</h2>" + "<ul>"
            + "<li>Each turn you may <b>Move</b>, <b>Pickup</b>, or <b>Attack</b> "
            + "(if you share a room with Doctor Lucky).</li>"
            + "<li>To attack successfully, Doctor Lucky must be unobserved by "
            + "any other player or the pet.</li>"
            + "<li>Click on the map (or on the highlighted legal rooms) to move—"
            + "no typing needed!</li>" + "</ul>" + "</html>";
        JOptionPane.showMessageDialog(view, instructions, "How to Play",
            JOptionPane.INFORMATION_MESSAGE);

        // ask human players
        String humanCountInput = JOptionPane.showInputDialog(view, "How many human players?",
            "Setup", JOptionPane.QUESTION_MESSAGE);
        int numHuman = Integer.parseInt(humanCountInput.trim());

        String names = JOptionPane.showInputDialog(view,
            "Enter " + numHuman + " name(s), comma‑separated:", "Setup",
            JOptionPane.QUESTION_MESSAGE);
        String[] humanNames = names.split("\\s*,\\s*");

        for (int i = 0; i < numHuman && i < humanNames.length; i++) {
          world.addPlayer(humanNames[i].trim(), 0);
          view.appendToLog("Added human player: " + humanNames[i].trim());
        }

        // ask AI players
        String aiCountInput = JOptionPane.showInputDialog(view, "How many computer players?",
            "Setup", JOptionPane.QUESTION_MESSAGE);
        int numAi = Integer.parseInt(aiCountInput.trim());

        for (int i = 1; i <= numAi; i++) {
          String aiName = "CPU" + i;
          world.addPlayer(aiName, 0);
          Iplayer justAdded = world.getPlayers().remove(world.getPlayers().size() - 1);
          ComputerPlayer cpu = new ComputerPlayer(aiName, justAdded.getPlayerLocation(), world,
              new RandomGenerator());
          world.getPlayers().add(cpu);
          view.appendToLog("Added AI player: " + aiName);
        }

        // start GUI controller
        new GuiController(world, view, finalMaxTurns);
        view.setVisible(true);

      } catch (IOException ioe) {
        // Only IOException is thrown by GameView constructor or GuiController init
        ioe.printStackTrace();
        System.exit(1);
      }
    });
  }
}
