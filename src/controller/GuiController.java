package controller;

import controller.commands.AttackCommand;
import controller.commands.DisplayPlayerCommand;
import controller.commands.LookCommand;
import controller.commands.MoveCommand;
import controller.commands.PickupCommand;
import controller.commands.SaveMapCommand;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import killdoctorlucky.model.ComputerPlayer;
import killdoctorlucky.model.Ipet;
import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Ispace;
import killdoctorlucky.model.Iworld;
import killdoctorlucky.model.World;
import view.GameView;
import view.IviewFeatures;

/**
 * Controller for the Kill Doctor Lucky GUI. Connects the Swing-based GameView
 * with the game model, handles all user actions, and advances Doctor Lucky and
 * the pet between turns.
 */
public class GuiController implements Icontroller, IviewFeatures {
  private static final int SCALE = 20;
  private final Iworld model;
  private final GameView view;
  private final int maxTurns;
  private int turnCount = 0;

  /**
   * Constructs a new GuiController.
   *
   * @param model    the game world model
   * @param view     the Swing-based game view
   * @param maxTurns the maximum number of turns before the game ends
   * @throws IOException if initialization of the view fails
   */
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

    // Kick off any computer turns until a human is up
    SwingUtilities.invokeLater(() -> processTurns(false));
  }

  private void redrawAll() {
    view.redrawMap(model.generateWorldMap());

    List<Iplayer> players = model.getPlayers();
    Ispace target = model.getTargetLocation();
    view.setEntities(players, target);

    if (model instanceof World) {
      Ipet pet = ((World) model).getPet();
      view.setPet(pet);
    }

    String current = players.get(turnCount % players.size()).getPlayerName();
    view.setStatusText(
        String.format("%s’s Turn — Turn %d — %s", current, turnCount, model.viewTargetCharacter()));
  }

  @Override
  public void startGame() {
    // Not used in GUI
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
    processTurns(false);
  }

  @Override
  public void handleMove() {
    List<Ispace> spaces = (model instanceof World) ? ((World) model).getSpaces()
        : Collections.emptyList();

    String[] names = spaces.stream().map(Ispace::getSpaceName).toArray(String[]::new);

    String choice = (String) JOptionPane.showInputDialog(view, "Choose a space to move to:", "Move",
        JOptionPane.PLAIN_MESSAGE, null, names, names.length > 0 ? names[0] : null);

    if (choice != null && !choice.isEmpty()) {
      handleMoveTo(choice);
    }
  }

  @Override
  public void handlePickup() {
    Iplayer me = model.getPlayers().get(turnCount % model.getPlayers().size());
    List<String> items = me.getPlayerLocation().getItems();
    if (items.isEmpty()) {
      JOptionPane.showMessageDialog(view, "No items here to pick up.");
      return;
    }
    String choice = (String) JOptionPane.showInputDialog(view, "Choose an item to pick up:",
        "Pickup", JOptionPane.PLAIN_MESSAGE, null, items.toArray(new String[0]), items.get(0));
    if (choice != null) {
      exec(new PickupCommand(getPlayer(), choice), true);
    }
  }

  @Override
  public void handleLook() {
    exec(new LookCommand(getPlayer()), true);
  }

  @Override
  public void handleAttack() {
    Iplayer me = model.getPlayers().get(turnCount % model.getPlayers().size());
    List<String> weapons = me.getPlayerItems();
    if (weapons.isEmpty()) {
      JOptionPane.showMessageDialog(view, "No weapons in inventory to attack with.");
      return;
    }
    String weapon = weapons.size() == 1 ? weapons.get(0)
        : (String) JOptionPane.showInputDialog(view, "Choose a weapon to attack with:", "Attack",
            JOptionPane.PLAIN_MESSAGE, null, weapons.toArray(new String[0]), weapons.get(0));
    if (weapon != null) {
      exec(new AttackCommand(getPlayer(), weapon), true);
    }
  }

  @Override
  public void handleDescribe() {
    exec(new DisplayPlayerCommand(getPlayer()), false);
  }

  @Override
  public void handleSaveMap() {
    String fileName = JOptionPane.showInputDialog(view, "Enter filename (e.g. worldmap.png):",
        "Save Map", JOptionPane.QUESTION_MESSAGE);
    if (fileName == null || fileName.trim().isEmpty()) {
      return;
    }
    exec(new SaveMapCommand(fileName.trim()), true);
  }

  @Override
  public void handleMovePet() {
    // Handled in processTurns
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
    // Check for player icon clicks
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
    // Otherwise check for room clicks
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

  private void exec(controller.commands.Icommand cmd, boolean consumesTurn) {
    try {
      cmd.execute(model);
      if (cmd instanceof MoveCommand) {
        view.appendToLog(getPlayer() + " moved to " + ((MoveCommand) cmd).getTargetSpaceName());
      } else if (cmd instanceof PickupCommand) {
        view.appendToLog(getPlayer() + " picked up " + ((PickupCommand) cmd).getItemName());
      } else if (cmd instanceof AttackCommand) {
        view.appendToLog(getPlayer() + " attacked with " + ((AttackCommand) cmd).getWeaponName());
      } else if (cmd instanceof LookCommand) {
        view.appendToLog(model.getSpaceInfo(getPlayer()));
      } else if (cmd instanceof DisplayPlayerCommand) {
        view.appendToLog(getPlayerInfo(getPlayer()));
      } else if (cmd instanceof SaveMapCommand) {
        view.appendToLog("Map saved.");
      }
    } catch (IllegalArgumentException ex) {
      view.appendToLog("Error: " + ex.getMessage());
    }

    redrawAll();

    if (consumesTurn) {
      processTurns(true);
    }
  }

  private void processTurns(boolean afterHuman) {
    new SwingWorker<Void, String>() {
      @Override
      protected Void doInBackground() {
        if (afterHuman) {
          advanceTargetAndPet();
          publish("--- After Turn " + turnCount);
        }
        List<Iplayer> players = model.getPlayers();
        while (turnCount < maxTurns && model.isGameNotOver()
            && players.get(turnCount % players.size()) instanceof ComputerPlayer) {

          ComputerPlayer cpu = (ComputerPlayer) players.get(turnCount % players.size());
          cpu.takeTurn();
          publish(cpu.getPlayerName() + " took its turn.");
          advanceTargetAndPet();
          publish("--- After Turn " + turnCount);
        }
        return null;
      }

      @Override
      protected void process(List<String> logs) {
        for (String line : logs) {
          view.appendToLog(line);
        }
        redrawAll();
      }
    }.execute();
  }

  private void advanceTargetAndPet() {
    model.moveTargetCharacter();
    if (model instanceof World) {
      ((World) model).movePetAutomatically();
    }
    turnCount++;
  }

  private String getPlayerInfo(String name) {
    int idx = model.findPlayerIndex(name);
    Iplayer p = model.getPlayers().get(idx);
    return "Player: " + p.getPlayerName() + "\n" + "Location: "
        + p.getPlayerLocation().getSpaceName() + "\n" + "Items: " + p.getPlayerItems();
  }

  private String getPlayer() {
    return model.getPlayers().get(turnCount % model.getPlayers().size()).getPlayerName();
  }
}
