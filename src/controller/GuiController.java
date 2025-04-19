package controller;

import controller.commands.*;
import killdoctorlucky.model.Ispace;
import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.TargetCharacter;
import killdoctorlucky.model.World;
import view.GameView;
import view.IViewFeatures;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Swing‐based controller for Kill Doctor Lucky (GUI).
 */
public class GuiController implements Icontroller, IViewFeatures {
  private final killdoctorlucky.model.Iworld model;
  private final GameView view;
  private final int maxTurns;
  private int turnCount = 0;
  private int currentPlayerIndex = 0;

  public GuiController(killdoctorlucky.model.Iworld model, GameView view, int maxTurns) {
    if (model == null || view == null) {
      throw new IllegalArgumentException("Model and view cannot be null.");
    }
    this.model = model;
    this.view = view;
    this.maxTurns = maxTurns;

    view.setViewFeatures(this);
    promptForPlayers();
    view.appendToLog("Welcome to Kill Doctor Lucky (GUI)!");
    redrawAll();
    view.setVisible(true);
  }

  private void promptForPlayers() {
    String input = JOptionPane.showInputDialog(view, "Enter player names (comma‑separated):",
        "Add Players", JOptionPane.QUESTION_MESSAGE);
    if (input != null && !input.trim().isEmpty()) {
      for (String raw : input.split(",")) {
        String name = raw.trim();
        if (!name.isEmpty()) {
          model.addPlayer(name, 0);
          view.appendToLog("Player added: " + name + " at Armory");
        }
      }
    }
    if (model.getPlayers().isEmpty()) {
      model.addPlayer("Player 1", 0);
      view.appendToLog("Player added: Player 1 at Armory");
    }
  }

  private void redrawAll() {
    BufferedImage map = model.generateWorldMap();
    // rotate player list so currentPlayerIndex → index 0
    List<Iplayer> raw = model.getPlayers();
    List<Iplayer> rotated = new ArrayList<>();
    if (!raw.isEmpty()) {
      rotated.add(raw.get(currentPlayerIndex));
      for (int i = 1; i < raw.size(); i++) {
        rotated.add(raw.get((currentPlayerIndex + i) % raw.size()));
      }
    }
    Ispace target = model.getTargetLocation();
    view.setEntities(rotated, target);
    view.redrawMap(map);
  }

  @Override
  public void startGame() throws IOException {
    /* unused */ }

  @Override
  public void handleNewWorld() {
    String path = JOptionPane.showInputDialog(view, "Enter path to new world file:");
    if (path != null && !path.isEmpty()) {
      try {
        killdoctorlucky.model.Iworld w2 = new World(path);
        new GuiController(w2, view, maxTurns);
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(view, "Failed to load: " + ex.getMessage());
      }
    }
  }

  @Override
  public void handleNewGame() {
    TargetCharacter.resetInstance();
    turnCount = 0;
    currentPlayerIndex = 0;
    view.appendToLog("Game restarted on same world.");
    redrawAll();
  }

  @Override
  public void handleQuit() {
    System.exit(0);
  }

  @Override
  public void handleNextTurn() {
    if (model.isGameNotOver() && turnCount < maxTurns) {
      model.moveTargetCharacter();
      if (model instanceof World) {
        ((World) model).movePetAutomatically();
      }
      Iplayer p = model.getPlayers().get(currentPlayerIndex);
      turnCount++;
      view.appendToLog("--- Turn " + turnCount + ": " + p.getPlayerName() + " ---");
      currentPlayerIndex = (currentPlayerIndex + 1) % model.getPlayers().size();
      redrawAll();
    } else {
      view.appendToLog("Game over or max turns reached.");
    }
  }

  @Override
  public void handleMove() {
    String dest = JOptionPane.showInputDialog(view, "Enter destination space:");
    if (dest != null)
      executeCommand(new MoveCommand(getPlayer(), dest));
  }

  @Override
  public void handleMoveTo(String spaceName) {
    executeCommand(new MoveCommand(getPlayer(), spaceName));
  }

  @Override
  public void handlePickup() {
    String item = JOptionPane.showInputDialog(view, "Enter item to pick up:");
    if (item != null)
      executeCommand(new PickupCommand(getPlayer(), item));
  }

  @Override
  public void handleLook() {
    String s = JOptionPane.showInputDialog(view, "Look at which space?");
    if (s != null)
      executeRunnable(() -> view.appendToLog(model.getSpaceInfo(s)));
  }

  @Override
  public void handleAttack() {
    String w = JOptionPane.showInputDialog(view, "Enter weapon to attack with:");
    if (w != null)
      executeCommand(new AttackCommand(getPlayer(), w));
  }

  @Override
  public void handleDescribe() {
    executeRunnable(() -> {
      String me = getPlayer();
      int idx = model.findPlayerIndex(me);
      Iplayer pp = model.getPlayers().get(idx);
      view.appendToLog("You: " + pp.getPlayerName() + " @ " + pp.getPlayerLocation().getSpaceName()
          + " | Items: " + pp.getPlayerItems());
    });
  }

  @Override
  public void handleSaveMap() {
    String fn = JOptionPane.showInputDialog(view, "Filename to save map:");
    if (fn != null)
      executeCommand(new SaveMapCommand(fn));
  }

  @Override
  public void handleMovePet() {
    String s = JOptionPane.showInputDialog(view, "Enter space for pet:");
    if (s != null)
      executeCommand(new MovePetCommand(s));
  }

  @Override
  public void handleMapClick(int x, int y) {
    String dest = JOptionPane.showInputDialog(view,
        "Clicked at (" + x + "," + y + "). Enter space name to move:");
    if (dest != null)
      handleMoveTo(dest);
  }

  /**
   * Wraps System.out/err during command execution, then logs everything into the
   * UI.
   */
  private void executeCommand(controller.commands.Icommand cmd) {
    // capture console
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream oldOut = System.out, oldErr = System.err;
    System.setOut(new PrintStream(baos));
    System.setErr(new PrintStream(baos));

    try {
      cmd.execute(model);
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      System.out.flush();
      System.err.flush();
      System.setOut(oldOut);
      System.setErr(oldErr);
    }

    String[] lines = baos.toString().split("\r?\n");
    for (String l : lines) {
      if (!l.isBlank())
        view.appendToLog(l);
    }

    redrawAll();
  }

  private void executeRunnable(Runnable r) {
    try {
      r.run();
    } catch (Exception ex) {
      view.appendToLog("Error: " + ex.getMessage());
    }
    redrawAll();
  }

  private String getPlayer() {
    return model.getPlayers().get(currentPlayerIndex).getPlayerName();
  }
}
