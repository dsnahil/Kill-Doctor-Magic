package controller;

import java.io.IOException;
import java.io.InputStreamReader;
import killdoctorlucky.model.ComputerPlayer;
import killdoctorlucky.model.Iworld;
import killdoctorlucky.model.World;
import util.RandomGenerator;

/**
 * Driver is the main entry point for the game. It combines the clean
 * welcome and help output from the Main class with the full controller-based
 * functionality, including human and computer players, the command design
 * pattern, and random number injection.
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
    // Parse command-line arguments for the world file and maximum turns.
    String filePath = args.length > 0 ? args[0] : "res/mansion.txt";
    final int maxTurns = args.length > 1 ? Integer.parseInt(args[1]) : 10;

    // Instantiate the model.
    Iworld world;
    try {
      world = new World(filePath);
    } catch (IOException e) {
      System.out.println("Failed to load world: " + e.getMessage());
      return;
    }

    // Print a clean welcome message and detailed help menu.
    System.out.println("Welcome to Kill Doctor Lucky!");
    System.out.println("World Loaded!");
    System.out.println("Mansion has been built successfully!");
    printHelp();

    // Add players.
    // (For cleaner output, remove debug prints from world.addPlayer if desired.)
    world.addPlayer("Alice", 0);
    world.addPlayer("Snahil", 1); // human-controlled player
    // Add a computer-controlled player.
    ComputerPlayer cpu = new ComputerPlayer("CPU", world.getSpaceByName("Kitchen"), world,
        new RandomGenerator());
    world.getPlayers().add(cpu);

    // Create the controller using a RandomGenerator instance (can be fixed for
    // testing).
    Icontroller controller = new ControllerImpl(world, new InputStreamReader(System.in), System.out,
        maxTurns, new RandomGenerator());

    // Start the game.
    try {
      controller.startGame();
    } catch (IOException e) {
      System.out.println("I/O Error in controller: " + e.getMessage());
    }
  }

  /**
   * Prints a detailed help menu that explains all available commands.
   */
  private static void printHelp() {
    System.out.println("\n=== Help Menu ===");
    System.out.println("move     - Moves your player to an adjacent room. "
        + "You will be shown valid neighbor rooms.");
    System.out.println("pickup   - Picks up an item from the current room. "
        + "The room's available items are listed.");
    System.out.println("attack   - Attacks Doctor Lucky using a weapon from your inventory. "
        + "Make sure you have picked up a valid weapon.");
    System.out.println("look     - Displays detailed information about your current space "
        + "(including visible neighbors).");
    System.out.println("describe - Displays your player details (location and inventory).");
    System.out.println("savemap  - Saves the current world map as a PNG file.");
    System.out.println("quit     - Exits the game.");
    System.out.println("Note: Commands and item/space names are case-insensitive. "
        + "Ensure you are in the correct room to pick up a specific item.");
    System.out.println("=================\n");
  }
}
