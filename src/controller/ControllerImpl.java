package controller;

import controller.commands.AttackCommand;
import controller.commands.DisplayPlayerCommand;
import controller.commands.LookCommand;
import controller.commands.MoveCommand;
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
   * Overload constructor if you want a default random-based version.
   */
  public ControllerImpl(Iworld model, Readable in, Appendable out, int maxTurns) {
    this(model, in, out, maxTurns, new RandomGenerator()); // uses real randomness by default
  }

  @Override
  public void startGame() throws IOException {
    Scanner sc = new Scanner(this.in);

    // Game loop
    while (model.isGameNotOver() && turnCount < maxTurns) {
      Iplayer currentPlayer = model.getPlayers().get(currentPlayerIndex);
      appendMessage("\n--- Turn " + (turnCount + 1) + " ---");
      appendMessage("Current Player: " + currentPlayer.getPlayerName());
      appendMessage("Doctor Lucky HP: " + TargetCharacter.getInstance().getTargetHealth());

      // If current player is a human, read their command:
      // If it's a computer-controlled player, you could auto-decide the command.
      if (isHumanPlayer(currentPlayer)) {
        appendMessage("Enter command (move, pickup, look, attack, describe, savemap, quit): ");
        if (!sc.hasNext()) {
          break;
        }
        String userCmd = sc.nextLine().trim().toLowerCase();

        if (userCmd.equals("quit")) {
          appendMessage("Exiting the game. Thanks for playing!");
          break;
        }

        // parse arguments if needed
        handleCommand(userCmd, sc, currentPlayer.getPlayerName());
      } else {
        ((ComputerPlayer) currentPlayer).takeTurn();
      }

      // Move Doctor Lucky automatically after each turn
      model.moveTargetCharacter();

      // Next player's turn
      turnCount++;
      currentPlayerIndex = (currentPlayerIndex + 1) % model.getPlayers().size();
    }

    // Check end conditions
    if (!model.isGameNotOver()) {
      appendMessage("Game Over! " + model.getWinner() + " has killed Doctor Lucky!");
    } else if (turnCount >= maxTurns) {
      appendMessage("Maximum number of turns reached. Game ends in a draw!");
    }
  }

  /**
   * Example method to determine if the current player is human or computer.
   */
  private boolean isHumanPlayer(Iplayer player) {
    // If player is an instance of ComputerPlayer, it’s controlled by the AI.
    return !(player instanceof killdoctorlucky.model.ComputerPlayer);
  }

  /**
   * Creates a command object based on user input.
   */
  private void handleCommand(String userCmd, Scanner sc, String playerName) throws IOException {
    Icommand command;
    switch (userCmd) {
      case "move":
        appendMessage("Enter space name: ");
        // Use nextLine() to read the full line and trim any leftover newline.
        String spaceName = sc.nextLine().trim();
        // If spaceName is still empty (because next() left an empty remainder), try
        // reading again:
        if (spaceName.isEmpty() && sc.hasNextLine()) {
          spaceName = sc.nextLine().trim();
        }
        command = new MoveCommand(playerName, spaceName);
        break;

      case "pickup":
        appendMessage("Enter item name: ");
        if (!sc.hasNext()) {
          return;
        }
        String itemName = sc.nextLine().trim();
        command = new PickupCommand(playerName, itemName);
        break;
      case "look":
        command = new LookCommand(playerName);
        break;
      case "attack":
        appendMessage("Enter weapon name: ");
        if (!sc.hasNext()) {
          return;
        }
        String weaponName = sc.nextLine().trim();
        command = new AttackCommand(playerName, weaponName);
        break;
      case "describe":
        command = new DisplayPlayerCommand(playerName);
        break;
      case "savemap":
        appendMessage("Enter filename (e.g. worldmap.png): ");
        if (!sc.hasNext()) {
          return;
        }
        String fileName = sc.next().trim();
        command = new SaveMapCommand(fileName);
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
   * Helper method to write a message to the Appendable output.
   */
  private void appendMessage(String msg) throws IOException {
    out.append(msg).append("\n");
  }
}
