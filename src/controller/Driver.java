package controller;

import java.io.IOException;
import java.io.InputStreamReader;
import killdoctorlucky.model.ComputerPlayer;
import killdoctorlucky.model.Iworld;
import killdoctorlucky.model.World;
import util.RandomGenerator;

/**
 * Driver is the main entry point for the Kill Doctor Lucky game. It sets up the
 * model and controller, then starts the game.
 */
public class Driver {
  /**
   * The main entry point for the Kill Doctor Lucky game.
   *
   * @param args Command-line arguments. The first argument (optional) is the path
   *             to the mansion file, and the second argument (optional) is the
   *             maximum number of turns.
   */
  public static void main(String[] args) {
    String filePath = args.length > 0 ? args[0] : "res/mansion.txt";
    final int maxTurns = args.length > 1 ? Integer.parseInt(args[1]) : 10;
    Iworld world;
    try {
      world = new World(filePath);
    } catch (IOException e) {
      System.out.println("Failed to load world: " + e.getMessage());
      return;
    }

    System.out.println("Welcome to Kill Doctor Lucky!");
    System.out.println("World Loaded!");
    System.out.println("Mansion has been built successfully!");
    printHelp();

    world.addPlayer("Tony", 0);
    world.addPlayer("Snahil", 1);
    ComputerPlayer cpu = new ComputerPlayer("CPU", world.getSpaceByName("Kitchen"), world,
        new RandomGenerator());
    world.getPlayers().add(cpu);

    Icontroller controller = new ControllerImpl(world, new InputStreamReader(System.in), System.out,
        maxTurns, new RandomGenerator());
    try {
      controller.startGame();
    } catch (IOException e) {
      System.out.println("I/O Error in controller: " + e.getMessage());
    }
  }

  /**
   * Prints a detailed help menu explaining all available commands.
   */
  private static void printHelp() {
    System.out.println("\n=== Help Menu ===");
    System.out.println(
        "move     - Moves your player to an adjacent room. "
        + "You will be shown valid neighbor rooms.");
    System.out.println(
        "pickup   - Picks up an item from the current room. "
        + "The room's available items are listed.");
    System.out.println(
        "attack   - Attacks the target using a weapon from your inventory. "
        + "Ensure you have a valid weapon.");
    System.out.println(
        "look     - Displays detailed information about your current space.");
    System.out.println("describe - Displays your player details (location and inventory).");
    System.out.println("savemap  - Saves the current world map as a PNG file.");
    System.out
        .println("movepet  - Moves the target character's pet to a specified adjacent space.");
    System.out.println("quit     - Exits the game.");
    System.out.println("Note: Commands and item/space names are case-insensitive.");
    System.out.println("=================\n");
  }
}
