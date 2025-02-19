package killdoctorlucky.model;

import java.io.IOException;
import java.util.Scanner;

/**
 * This class represents the main entry point for the game. It initializes the
 * world and starts the game loop.
 */
public class Main {
  /**
   * The main entry point for the Kill Doctor Lucky game.
   *
   * @param args Command-line arguments. If no file path is provided,
   *             "res/mansion.txt" is used.
   */
  public static void main(String[] args) {
    String filePath = args.length > 0 ? args[0] : "res/mansion.txt";

    try (Scanner scanner = new Scanner(System.in)) {
      final Iworld world = new World(filePath);
      System.out.println("Welcome to Kill Doctor Lucky!");
      System.out.println("World Loaded! ");
      System.out.println("Mansion has been built successfully!");

      // Display a detailed help message at startup.
      printHelp();

      // Add a player at space 0 (for example, Armory)
      world.addPlayer("Player1", 0);
      Iplayer player = world.getPlayers().get(0);
      ItargetCharacter doctorLucky = TargetCharacter.getInstance();

      while (world.isGameNotOver()) {
        System.out.println("\nYou are in: " + player.getPlayerLocation().getSpaceName());
        System.out.println("Your Items: " + player.getPlayerItems());
        System.out.println("Doctor Lucky's HP: " + doctorLucky.getTargetHealth());
        System.out.println("Available commands: move, pickup, attack, help, quit");
        System.out.print("Enter command: ");

        String command = scanner.nextLine().toLowerCase().trim();

        switch (command) {
          case "move":
            System.out.println("Valid neighbors: " + player.getPlayerLocation().getNeighbors());
            System.out
                .println("Enter space name to move to (type exactly one of the valid neighbors):");
            String spaceName = scanner.nextLine().trim();
            if (spaceName.isEmpty()) {
              System.out.println("Invalid input: Space name cannot be empty.");
              continue;
            }
            Ispace newSpace = world.getSpaceByName(spaceName);
            if (newSpace == null) {
              System.out.println("Error: No such space exists.");
              continue;
            }
            try {
              player.moveTo(newSpace);
            } catch (IllegalArgumentException e) {
              System.out.println("Invalid move: " + e.getMessage());
            }

            break;

          case "pickup":
            System.out.println("Items in this room: " + player.getPlayerLocation().getItems());
            System.out
                .println("Enter item name to pick up (type exactly one of the listed items):");
            String itemName = scanner.nextLine().trim();
            try {
              player.pickUpItem(itemName);
            } catch (IllegalArgumentException e) {
              System.out.println("Cannot pick up item: " + e.getMessage());
            }

            break;

          case "attack":
            System.out.println("Your current inventory: " + player.getPlayerItems());
            System.out.println(
                "Enter weapon name to attack Doctor Lucky (choose one from your inventory):");
            String weapon = scanner.nextLine().trim();
            try {
              player.attackDoctorLucky(weapon);
            } catch (IllegalArgumentException e) {
              System.out.println("Attack failed: " + e.getMessage());
            }

            break;

          case "help":
            printHelp();
            break;

          case "quit":
            System.out.println("Exiting the game. Thanks for playing!");
            return;

          default:
            System.out.println("Unrecognized command. Type 'help' for a list of valid commands.");
            break;
        }
        world.moveTargetCharacter();
        System.out.println("Doctor Lucky moved to: " + world.getTargetLocation().getSpaceName());
      }

      System.out.println("\nGame Over! " + world.getWinner() + " won by killing Doctor Lucky!");

    } catch (IOException e) {
      System.out.println("I/O Error: " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.out.println("Error: " + e.getMessage());
    }
  }

  private static void printHelp() {
    System.out.println("\n=== Help Menu ===");
    System.out.println(
        "move   - Moves your player to an adjacent room. You will be shown valid neighbor rooms.");
    System.out.println(
        "pickup - Picks up an item from the current room. The room's available items are listed.");
    System.out.println("attack - Attacks Doctor Lucky using a weapon from your inventory. "
        + "Make sure you have picked up a valid weapon.");
    System.out.println("help   - Displays this help menu with instructions for each command.");
    System.out.println("quit   - Exits the game.");
    System.out.println("Note: Commands and item/space names are case-insensitive. "
        + "Make sure you are in the correct room to pick up a specific item.");
    System.out.println("=================\n");
  }
}
