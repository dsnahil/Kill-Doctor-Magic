package controller;

import controller.commands.AttackCommand;
import controller.commands.DisplayPlayerCommand;
import controller.commands.MoveCommand;
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
import view.IviewFeatures;

/**
 * Coordinates between the Swing GUI and the game model, handling all user
 * actions.
 */
public class GuiController implements Icontroller, IviewFeatures {

  private final Iworld model;
  private final GameView view;
  private final int maxTurns;
  private int turnCount = 0;

  /**
   * Constructs the GUI controller.
   *
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
    view.appendToLog("Welcome to Kill Doctor Lucky!\n"
        + "• Move by clicking the Move button, then type or click a room.\n"
        + "• Pickup items via Pickup button.\n"
        + "• Attack only when in same room and unseen; pet blocks attacks.\n"
        + "• Pet wanders automatically each turn.\n");
    redrawAll();
  }

  private void redrawAll() {
    BufferedImage map = model.generateWorldMap();
    List<Iplayer> players = model.getPlayers();
    Ispace target = model.getTargetLocation();

    view.redrawMap(map);
    view.setEntities(players, target);
    view.setStatusText(String.format("%s’s Turn — Turn %d — %s",
        players.get(turnCount % players.size()).getPlayerName(), turnCount,
        model.viewTargetCharacter()));
  }

  @Override
  public void startGame() throws IOException {
    // Not used in GUI mode
  }

  @Override
  public void handleNewGame() {
    try {
      new GuiController(model, view, maxTurns);
    } catch (IOException ex) {
      view.appendToLog("Restart failed: " + ex.getMessage());
    }
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
    String dest = JOptionPane.showInputDialog(view, "Enter destination space:");
    if (dest != null) {
      exec(new MoveCommand(getPlayer(), dest), true);
    }
  }

  @Override
  public void handlePickup() {
    String item = JOptionPane.showInputDialog(view, "Enter item to pick up:");
    if (item != null) {
      exec(new PickupCommand(getPlayer(), item), true);
    }
  }

  @Override
  public void handleLook() {
    String where = JOptionPane.showInputDialog(view, "Look at which space?");
    if (where != null) {
      view.appendToLog(model.getSpaceInfo(where));
    }
  }

  @Override
  public void handleAttack() {
    String w = JOptionPane.showInputDialog(view, "Enter weapon to attack with:");
    if (w != null) {
      exec(new AttackCommand(getPlayer(), w), true);
    }
  }

  @Override
  public void handleDescribe() {
    exec(new DisplayPlayerCommand(getPlayer()), false);
  }

  @Override
  public void handleSaveMap() {
    String fn = JOptionPane.showInputDialog(view, "Filename to save map (e.g. map.png):");
    if (fn != null) {
      exec(new SaveMapCommand(fn), false);
    }
  }

  @Override
  public void handleMovePet() {
    // No-op: pet moves automatically
  }

  @Override
  public void handleInventoryClick(String itemName) {
    exec(new AttackCommand(getPlayer(), itemName), true);
  }

  @Override
  public void handleMoveTo(String spaceName) {
    exec(new MoveCommand(getPlayer(), spaceName), true);
  }

  @Override
  public void handleMapClick(int x, int y) {
    // Unused
  }

  private void exec(controller.commands.Icommand cmd, boolean consumesTurn) {
    try {
      cmd.execute(model);
    } catch (Exception ex) {
      view.appendToLog("Error: " + ex.getMessage());
    }
    if (consumesTurn) {
      handleNextTurn();
    } else {
      redrawAll();
    }
  }

  private String getPlayer() {
    return model.getPlayers().get(turnCount % model.getPlayers().size()).getPlayerName();
  }
}
