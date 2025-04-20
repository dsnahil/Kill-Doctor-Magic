package controller;

import controller.commands.AttackCommand;
import controller.commands.DisplayPlayerCommand;
import controller.commands.LookCommand;
import controller.commands.MoveCommand;
import controller.commands.MovePetCommand;
import controller.commands.PickupCommand;
import controller.commands.SaveMapCommand;
import java.awt.Rectangle;
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
 * GUI controller handling user interactions via Swing.
 */
public class GuiController implements Icontroller, IviewFeatures {
  private final Iworld model;
  private final GameView view;
  private final int maxTurns;
  private int turnCount = 0;
  private static final int SCALE = 10;

  public GuiController(Iworld model, GameView view, int maxTurns) throws IOException {
    if (model == null || view == null) {
      throw new IllegalArgumentException("Model and view cannot be null.");
    }
    this.model = model;
    this.view = view;
    this.maxTurns = maxTurns;

    view.setViewFeatures(this);
    view.appendToLog("Welcome to Kill Doctor Lucky!\n" + "• Move by clicking the map or 'M'.\n"
        + "• Pickup items via button or 'P'.\n" + "• Look via button or 'L'.\n"
        + "• Attack via button or 'A'.\n");
    redrawAll();
  }

  private void redrawAll() {
    view.redrawMap(model.generateWorldMap());
    List<Iplayer> players = model.getPlayers();
    Ispace target = model.getTargetLocation();
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
      view.appendToLog("--- After Turn " + turnCount);
      redrawAll();
    } else {
      view.appendToLog("Game Over or max turns reached.");
    }
  }

  @Override
  public void handleMove() {
    // Not used: map-click based movement now
  }

  @Override
  public void handlePickup() {
    exec(new PickupCommand(getPlayer(), null), true);
  }

  @Override
  public void handleLook() {
    exec(new LookCommand(getPlayer()), false);
  }

  @Override
  public void handleAttack() {
    exec(new AttackCommand(getPlayer(), null), true);
  }

  @Override
  public void handleDescribe() {
    exec(new DisplayPlayerCommand(getPlayer()), false);
  }

  @Override
  public void handleSaveMap() {
    exec(new SaveMapCommand(null), false);
  }

  @Override
  public void handleMovePet() {
    // pet moves automatically
  }

  @Override
  public void handleInventoryClick(String itemName) {
    exec(new AttackCommand(getPlayer(), itemName), true);
  }

  @Override
  public void handleMoveTo(String spaceName) {
    exec(new MoveCommand(getPlayer(), spaceName), true);
  }

  /**
   * Called when the map panel is clicked (x,y in pixels).
   */
  @Override
  public void handleMapClick(int x, int y) {
    // 1) Check if clicked on a player icon
    for (Iplayer p : model.getPlayers()) {
      Ispace loc = p.getPlayerLocation();
      int cx = ((loc.getUpperColumn() + loc.getLowerColumn()) / 2) * SCALE;
      int cy = ((loc.getUpperRow() + loc.getLowerRow()) / 2) * SCALE;
      Rectangle icon = new Rectangle(cx - 5, cy - 5, 10, 10);
      if (icon.contains(x, y)) {
        JOptionPane.showMessageDialog(view, getPlayerInfo(p.getPlayerName()), "Player Info",
            JOptionPane.INFORMATION_MESSAGE);
        return;
      }
    }
    // 2) Check if clicked inside any room to move there
    if (model instanceof World) {
      for (Ispace s : ((World) model).getSpaces()) {
        int rx = s.getUpperColumn() * SCALE;
        int ry = s.getUpperRow() * SCALE;
        int rw = (s.getLowerColumn() - s.getUpperColumn()) * SCALE;
        int rh = (s.getLowerRow() - s.getUpperRow()) * SCALE;
        Rectangle roomRect = new Rectangle(rx, ry, rw, rh);
        if (roomRect.contains(x, y)) {
          int ans = JOptionPane.showConfirmDialog(view, "Move to " + s.getSpaceName() + "?",
              "Confirm Move", JOptionPane.YES_NO_OPTION);
          if (ans == JOptionPane.YES_OPTION) {
            handleMoveTo(s.getSpaceName());
          }
          return;
        }
      }
    }
  }

  /**
   * Executes a command and logs the result in the GUI.
   */
  private void exec(controller.commands.Icommand cmd, boolean consumesTurn) {
    try {
      cmd.execute(model);
      if (cmd instanceof MoveCommand) {
        String dest = ((MoveCommand) cmd).getTargetSpaceName();
        view.appendToLog(getPlayer() + " moved to " + dest);
      } else if (cmd instanceof PickupCommand) {
        String item = ((PickupCommand) cmd).getItemName();
        view.appendToLog(getPlayer() + " picked up " + item);
      } else if (cmd instanceof AttackCommand) {
        String w = ((AttackCommand) cmd).getWeaponName();
        view.appendToLog(getPlayer() + " attacked with " + w);
      } else if (cmd instanceof LookCommand) {
        view.appendToLog(model.getSpaceInfo(getPlayer()));
      } else if (cmd instanceof DisplayPlayerCommand) {
        view.appendToLog(getPlayerInfo(getPlayer()));
      } else if (cmd instanceof SaveMapCommand) {
        view.appendToLog("Map saved.");
      } else if (cmd instanceof MovePetCommand) {
        view.appendToLog("Pet moved.");
      }
    } catch (Exception ex) {
      view.appendToLog("Error: " + ex.getMessage());
    }
    if (consumesTurn) {
      handleNextTurn();
    } else {
      redrawAll();
    }
  }

  /**
   * Formats a player's info for display.
   */
  private String getPlayerInfo(String name) {
    int idx = model.findPlayerIndex(name);
    Iplayer p = model.getPlayers().get(idx);
    return "Player: " + p.getPlayerName() + "\n" + "Location: "
        + p.getPlayerLocation().getSpaceName() + "\n" + "Items: " + p.getPlayerItems();
  }

  /**
   * Returns the current human player's name.
   */
  private String getPlayer() {
    return model.getPlayers().get(turnCount % model.getPlayers().size()).getPlayerName();
  }
}
