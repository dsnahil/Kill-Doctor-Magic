package killdoctorlucky.model;

import java.util.List;
import java.util.Random;

/**
 * Represents a computer-controlled player in the "Kill Doctor Lucky" game. The
 * computer player randomly chooses an action each turn, including moving,
 * picking up an item, attacking, or looking around.
 */
public class ComputerPlayer extends Player {

  private final Random rand;

  /**
   * Constructs a ComputerPlayer with a specified name, starting location, and
   * game world.
   *
   * @param name          the name of the computer player.
   * @param startLocation the starting space of the player in the world.
   * @param world         the game world where the player exists.
   */
  public ComputerPlayer(String name, Ispace startLocation, Iworld world) {
    super(name, startLocation, world);
    this.rand = new Random();
  }

  /**
   * Determines and executes the next action for the computer-controlled player.
   * The AI randomly selects between moving, picking up an item, attacking Doctor
   * Lucky, or simply looking around.
   */
  public void takeTurn() {
    int choice = rand.nextInt(4); // Randomly choose 0, 1, 2, or 3

    switch (choice) {
      case 0:
        // Move: choose a random neighbor if available
        List<String> neighbors = getPlayerLocation().getNeighbors();
        if (!neighbors.isEmpty()) {
          String target = neighbors.get(rand.nextInt(neighbors.size()));
          moveTo(world.getSpaceByName(target));
          System.out.println(getPlayerName() + " moves to " + target);
          return;
        }
        break;

      case 1:
        // Pickup: choose a random item from current space, if any
        List<String> items = getPlayerLocation().getItems();
        if (!items.isEmpty()) {
          String item = items.get(rand.nextInt(items.size()));
          pickUpItem(item);
          System.out.println(getPlayerName() + " picks up " + item);
          return;
        }
        break;

      case 2:
        // Attack: if inventory is not empty, choose a random weapon
        List<String> inventory = getPlayerItems();
        if (!inventory.isEmpty()) {
          String weapon = inventory.get(rand.nextInt(inventory.size()));
          attackDoctorLucky(weapon);
          System.out.println(getPlayerName() + " attacks Doctor Lucky with " + weapon);
          return;
        }
        break;

      default:
        // Look around as default action
        System.out.println(getPlayerName() + " looks around.");
        break;
    }
  }
}
