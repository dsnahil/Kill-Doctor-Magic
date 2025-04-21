package controller;

import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import killdoctorlucky.model.ComputerPlayer;
import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Iworld;
import killdoctorlucky.model.World;
import util.RandomGenerator;
import view.GameView;

/**
 * Driver class to launch the Kill Doctor Lucky GUI.
 *
 * Usage: java controller.Driver [world‑file] [maxTurns]
 */
public class Driver {

  /**
   * Entry point for the Kill Doctor Lucky application.
   *
   * @param args command-line arguments specifying args[0] = path to world file
   *             (optional, defaults to "res/mansion.txt") args[1] = max number of
   *             turns (optional, defaults to 20)
   */
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception ignore) {
    }

    // 1) Determine world file
    String path = (args.length > 0) ? args[0] : "res/mansion.txt";

    // 2) Parse maxTurns if provided
    int maxTurns = 20;
    if (args.length > 1) {
      try {
        maxTurns = Integer.parseInt(args[1]);
      } catch (NumberFormatException e) {
        System.err.println("Invalid maxTurns '" + args[1] + "', using default 20.");
      }
    }
    // freeze it for the lambda
    final int finalMaxTurns = maxTurns;

    // 3) Load the world
    final Iworld world;
    try {
      world = new World(path);
    } catch (IOException ex) {
      System.err.println("Failed to load world: " + ex.getMessage());
      return;
    }

    // 4) Launch the GUI on the EDT
    SwingUtilities.invokeLater(() -> {
      try {
        GameView view = new GameView();

        // show instructions dialog
        String instructions = "<html>" + "<h2>Welcome to Kill Doctor Lucky!</h2>" + "<ul>"
            + "<li>Each turn you may <b>Move</b>, <b>Pickup</b>, or <b>Attack</b> (if you share a room with Doctor Lucky).</li>"
            + "<li>To attack successfully, Doctor Lucky must be unobserved by any other player or the pet.</li>"
            + "<li>Click on the map (or on the highlighted legal rooms) to move—no typing needed!</li>"
            + "</ul>" + "</html>";
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
          // first add a placeholder Player so world.setup side‑effects run
          world.addPlayer(aiName, 0);
          // swap it out for our ComputerPlayer
          Iplayer justAdded = world.getPlayers().remove(world.getPlayers().size() - 1);
          ComputerPlayer cpu = new ComputerPlayer(aiName, justAdded.getPlayerLocation(), world,
              new RandomGenerator());
          world.getPlayers().add(cpu);
          view.appendToLog("Added AI player: " + aiName);
        }

        // start GUI controller with the frozen-final maxTurns
        new GuiController(world, view, finalMaxTurns);

        // finally show window
        view.setVisible(true);

      } catch (Exception ex) {
        ex.printStackTrace();
        System.exit(1);
      }
    });
  }
}
