package controller;

import controller.commands.AttackCommand;
import controller.commands.DisplayPlayerCommand;
import controller.commands.LookCommand;
import controller.commands.MoveCommand;
import controller.commands.MovePetCommand;
import controller.commands.PickupCommand;
import controller.commands.SaveMapCommand;
import java.io.IOException;
import java.util.Scanner;
import killdoctorlucky.model.ComputerPlayer;
import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Iworld;
import killdoctorlucky.model.TargetCharacter;
import util.RandomGenerator;

/**
 * A text-based controller that reads commands from input and executes them
 * synchronously.
 */
public class ControllerImpl implements Icontroller {

  private final Iworld model;
  private final Readable in;
  private final Appendable out;
  private final int maxTurns;

  private int turnCount = 0;
  private int currentPlayerIndex = 0;

  /**
   * Constructs a ControllerImpl.
   *
   * @param model    the game world model
   * @param in       a Readable source (e.g., System.in)
   * @param out      an Appendable sink (e.g., System.out)
   * @param maxTurns the maximum number of turns allowed before the game ends
   * @param randGen  a RandomGenerator instance for controlling randomness
   */
  public ControllerImpl(Iworld model, Readable in, Appendable out, int maxTurns,
      RandomGenerator randGen) {
    if (model == null || in == null || out == null) {
      throw new IllegalArgumentException("Model/input/output cannot be null.");
    }
    this.model = model;
    this.in = in;
    this.out = out;
    this.maxTurns = maxTurns;
  }

  /**
   * Overloaded constructor using a default random generator.
   */
  public ControllerImpl(Iworld model, Readable in, Appendable out, int maxTurns) {
    this(model, in, out, maxTurns, new RandomGenerator());
  }

  @Override
  public void startGame() throws IOException {
    Scanner sc = new Scanner(this.in);

    // Game loop
    while (model.isGameNotOver() && turnCount < maxTurns) {
      Iplayer currentPlayer = model.getPlayers().get(currentPlayerIndex);
      appendMessage("\n--- Turn " + (turnCount + 1) + " ---");
      appendMessage("Current Player: " + currentPlayer.getPlayerName());
      // New: Display concise target info.
      appendMessage("Target: " + model.viewTargetCharacter());

      if (isHumanPlayer(currentPlayer)) {
        appendMessage(
            "Enter command (move, pickup, look, attack, describe, savemap, movepet, quit): ");
        if (!sc.hasNext()) {
          break;
        }
        String userCmd = sc.nextLine().trim().toLowerCase();
        if (userCmd.equals("quit")) {
          appendMessage("Exiting the game. Thanks for playing!");
          break;
        }
        handleCommand(userCmd, sc, currentPlayer.getPlayerName());
      } else {
        ((ComputerPlayer) currentPlayer).takeTurn();
      }

      // Move target automatically after each turn.
      model.moveTargetCharacter();

      turnCount++;
      currentPlayerIndex = (currentPlayerIndex + 1) % model.getPlayers().size();
    }

    if (!model.isGameNotOver()) {
      appendMessage("Game Over! " + model.getWinner() + " has killed "
          + TargetCharacter.getInstance().getTargetName() + "!");
    } else if (turnCount >= maxTurns) {
      appendMessage("Maximum number of turns reached. Game ends in a draw!");
    }
  }

  /**
   * Determines if the given player is human-controlled.
   *
   * @param player the player to check.
   * @return true if human-controlled, false otherwise.
   */
  private boolean isHumanPlayer(Iplayer player) {
    return !(player instanceof killdoctorlucky.model.ComputerPlayer);
  }

  /**
   * Parses the user command and creates the corresponding command object.
   *
   * @param userCmd    the command string entered by the user.
   * @param sc         the Scanner for additional input.
   * @param playerName the name of the current player.
   * @throws IOException if writing to the output fails.
   */
  private void handleCommand(String userCmd, Scanner sc, String playerName) throws IOException {
    controller.Icommand command;
    switch (userCmd) {
      case "move":
        appendMessage("Enter space name: ");
        String spaceName = sc.nextLine().trim();
        if (spaceName.isEmpty() && sc.hasNextLine()) {
          spaceName = sc.nextLine().trim();
        }
        command = new MoveCommand(playerName, spaceName);
        break;
      case "pickup":
        appendMessage("Enter item name: ");
        String itemName = sc.nextLine().trim();
        command = new PickupCommand(playerName, itemName);
        break;
      case "look":
        command = new LookCommand(playerName);
        break;
      case "attack":
        appendMessage("Enter weapon name: ");
        String weaponName = sc.nextLine().trim();
        command = new AttackCommand(playerName, weaponName);
        break;
      case "describe":
        command = new DisplayPlayerCommand(playerName);
        break;
      case "savemap":
        appendMessage("Enter filename (e.g. worldmap.png): ");
        String fileName = sc.nextLine().trim();
        command = new SaveMapCommand(fileName);
        break;
      case "movepet":
        // New: Command to move the target's pet.
        appendMessage("Enter pet target space: ");
        String petSpaceName = sc.nextLine().trim();
        command = new MovePetCommand(petSpaceName);
        break;
      default:
        appendMessage("Unknown command.");
        return;
    }
    try {
      command.execute(model);
    } catch (IllegalArgumentException e) {
      appendMessage("Command failed: " + e.getMessage());
    }
  }

  /**
   * Appends a message to the output.
   *
   * @param msg the message to append.
   * @throws IOException if writing fails.
   */
  private void appendMessage(String msg) throws IOException {
    out.append(msg).append("\n");
  }
}
