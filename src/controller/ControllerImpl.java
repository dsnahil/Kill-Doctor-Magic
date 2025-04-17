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
      // Print turn header only once per turn.
      appendMessage("\n--- Turn " + (turnCount + 1) + " ---");
      appendMessage("Current Player: " + currentPlayer.getPlayerName());
      appendMessage("Target: " + model.viewTargetCharacter());

      boolean commandConsumesTurn = false;
      if (isHumanPlayer(currentPlayer)) {
        // Loop within the turn until a command that consumes the turn is executed.
        while (!commandConsumesTurn) {
          appendMessage(
              "Enter command (move, pickup, look, attack, describe, savemap, movepet, quit): ");
          if (!sc.hasNext()) {
            break;
          }
          String userCmd = sc.nextLine().trim().toLowerCase();
          if ("quit".equals(userCmd)) {
            appendMessage("Exiting the game. Thanks for playing!");
            return;
          }
          // Execute command; if it does not consume a turn, prompt again.
          commandConsumesTurn = handleCommand(userCmd, sc, currentPlayer.getPlayerName());
        }
      } else {
        ((ComputerPlayer) currentPlayer).takeTurn();
        commandConsumesTurn = true;
      }

      // Only update game state if a turn-consuming command was executed.
      if (commandConsumesTurn) {
        model.moveTargetCharacter();

        // Extra credit: move the pet automatically (wandering pet)
        if (model instanceof killdoctorlucky.model.World) {
          ((killdoctorlucky.model.World) model).movePetAutomatically();
        }

        turnCount++;
        currentPlayerIndex = (currentPlayerIndex + 1) % model.getPlayers().size();
      }
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
   * @return true if the command should consume a turn; false otherwise.
   * @throws IOException if writing to the output fails.
   */
  private boolean handleCommand(String userCmd, Scanner sc, String playerName) throws IOException {
    controller.commands.Icommand command;
    // By default, commands consume a turn.
    boolean consumesTurn = true;
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
        // savemap is a free command; it should not consume the turn.
        consumesTurn = false;
        break;
      case "movepet":
        appendMessage("Enter pet target space: ");
        String petSpaceName = sc.nextLine().trim();
        command = new MovePetCommand(petSpaceName);
        break;
      default:
        appendMessage("Unknown command.");
        // Unknown commands do not consume a turn.
        return false;
    }
    try {
      command.execute(model);
    } catch (IllegalArgumentException e) {
      appendMessage("Command failed: " + e.getMessage());
    }
    return consumesTurn;
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
