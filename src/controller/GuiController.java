// File: src/controller/GuiController.java
package controller;

import controller.commands.AttackCommand;
import controller.commands.MoveCommand;
import controller.commands.MovePetCommand;
import controller.commands.PickupCommand;
import controller.commands.SaveMapCommand;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import javax.swing.JOptionPane;
import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Ispace;
import killdoctorlucky.model.Iworld;
import killdoctorlucky.model.World;
import view.GameView;
import view.IViewFeatures;

/**
 * GUI controller. After each human action that consumes a turn, automatically
 * moves Doctor Lucky + pet, increments turn count, and updates the header/log.
 */
public class GuiController implements Icontroller, IViewFeatures {

  private final Iworld model;
  private final GameView view;
  private final int maxTurns;
  private int turnCount = 0;

  /**
   * @param model    the world model
   * @param view     the Swing UI
   * @param maxTurns max turns before draw
   * @throws IOException if initial map fails
   */
  public GuiController(Iworld model, GameView view, int maxTurns) throws IOException {
    if (model == null || view == null) {
      throw new IllegalArgumentException("Model and view cannot be null.");
    }
    this.model = model;
    this.view = view;
    this.maxTurns = maxTurns;

    view.setViewFeatures(this);
    redrawAll();
  }

  private void redrawAll() {
    BufferedImage map = model.generateWorldMap();
    List<Iplayer> players = model.getPlayers();
    Ispace target = model.getTargetLocation();
    view.redrawMap(map);
    view.setEntities(players, target);

    String status = String.format("Turn: %d    %s", turnCount, model.viewTargetCharacter());
    view.appendToLog("Turn " + turnCount + ": " + model.viewTargetCharacter());
  }

  @Override
  public void startGame() {
    /* unused */ }

  @Override
  public void handleNewGame() {
    turnCount = 0;
    view.appendToLog("Game restarted on same world.");
    redrawAll();
  }

  @Override
  public void handleNewWorld() {
    String path = JOptionPane.showInputDialog(view, "Enter path to new world file:");
    if (path != null && !path.isEmpty()) {
      try {
        Iworld w2 = new World(path);
        new GuiController(w2, view, maxTurns);
      } catch (IOException ex) {
        JOptionPane.showMessageDialog(view, "Load failed: " + ex.getMessage());
      }
    }
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
      turnCount++;
      view.appendToLog("--- After Turn " + turnCount + " ---");
      redrawAll();
    } else {
      view.appendToLog("Game Over or max turns reached.");
    }
  }

  @Override
  public void handleMove() {
    String dest = JOptionPane.showInputDialog("Enter destination space:");
    if (dest != null)
      exec(new MoveCommand(getPlayer(), dest), true);
  }

  @Override
  public void handlePickup() {
    String item = JOptionPane.showInputDialog("Enter item to pick up:");
    if (item != null)
      exec(new PickupCommand(getPlayer(), item), true);
  }

  @Override
  public void handleAttack() {
    String w = JOptionPane.showInputDialog("Enter weapon to attack with:");
    if (w != null)
      exec(new AttackCommand(getPlayer(), w), true);
  }

  @Override
  public void handleMovePet() {
    String s = JOptionPane.showInputDialog("Enter space to move pet to:");
    if (s != null)
      exec(new MovePetCommand(s), true);
  }

  @Override
  public void handleMoveTo(String spaceName) {
    exec(new MoveCommand(getPlayer(), spaceName), true);
  }

  @Override
  public void handleLook() {
    String where = JOptionPane.showInputDialog("Look at which space?");
    if (where != null)
      view.appendToLog(model.getSpaceInfo(where));
  }

  @Override
  public void handleDescribe() {
    String me = getPlayer();
    int idx = model.findPlayerIndex(me);
    Iplayer p = model.getPlayers().get(idx);
    view.appendToLog(String.format("%s @ %s    Items: %s", me, p.getPlayerLocation().getSpaceName(),
        p.getPlayerItems()));
  }

  @Override
  public void handleSaveMap() {
    String fn = JOptionPane.showInputDialog("Filename to save map (e.g. map.png):");
    if (fn != null)
      exec(new SaveMapCommand(fn), false);
  }

  @Override
  public void handleMapClick(int x, int y) {
    String dest = JOptionPane.showInputDialog(String.format("Clicked at (%d,%d). Enter space to move to:", x, y));
    if (dest != null)
      exec(new MoveCommand(getPlayer(), dest), true);
  }

  private void exec(controller.commands.Icommand cmd, boolean consumesTurn) {
    try {
      cmd.execute(model);
    } catch (Exception ex) {
      view.appendToLog("Error: " + ex.getMessage());
    }
    if (consumesTurn)
      handleNextTurn();
    else
      redrawAll();
  }

  private String getPlayer() {
    return model.getPlayers().get(0).getPlayerName();
  }
}
