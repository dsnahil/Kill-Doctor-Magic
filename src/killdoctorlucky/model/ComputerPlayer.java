package killdoctorlucky.model;

import java.util.List;
import util.RandomGenerator;

/**
 * Represents a computer-controlled player in the "Kill Doctor Lucky" game.
 */
public class ComputerPlayer extends Player {
  private final RandomGenerator randGen;

  /**
   * Constructs a ComputerPlayer with a specified name, starting location, and
   * game world, using a real RandomGenerator by default.
   *
   * @param name          the name of the computer player
   * @param startLocation the starting space for the player
   * @param world         the game world
   */
  public ComputerPlayer(String name, Ispace startLocation, Iworld world) {
    this(name, startLocation, world, new RandomGenerator());
  }

  /**
   * Constructs a ComputerPlayer with a specified random generator, allowing you
   * to control randomness for testing or real game play.
   *
   * @param name          the name of the computer player
   * @param startLocation the starting space for the player
   * @param world         the game world
   * @param randGen       a RandomGenerator (could be real or fixed)
   */
  public ComputerPlayer(String name, Ispace startLocation, Iworld world, RandomGenerator randGen) {
    super(name, startLocation, world);
    this.randGen = randGen;
  }

  /**
   * Determines and executes the next action for the computer-controlled player.
   * The AI randomly selects between moving, picking up an item, attacking, or
   * looking around.
   */
  public void takeTurn() {
    int choice = randGen.nextInt(4); // randomly pick 0,1,2,3
    switch (choice) {
      case 0:
        // Move: choose a random neighbor if available
        List<String> neighbors = getPlayerLocation().getNeighbors();
        if (!neighbors.isEmpty()) {
          String target = neighbors.get(randGen.nextInt(neighbors.size()));
          moveTo(world.getSpaceByName(target));
          System.out.println(getPlayerName() + " moves to " + target);
        } else {
          System.out.println(getPlayerName() + " looks around (no neighbors).");
        }
        break;
      case 1:
        // Pickup: choose a random item from current space, if any
        List<String> items = getPlayerLocation().getItems();
        if (!items.isEmpty()) {
          String item = items.get(randGen.nextInt(items.size()));
          pickUpItem(item);
          System.out.println(getPlayerName() + " picks up " + item);
        } else {
          System.out.println(getPlayerName() + " looks around (no items).");
        }
        break;
      case 2:
        // Attack: if inventory is not empty, pick a random weapon
        List<String> inventory = getPlayerItems();
        if (!inventory.isEmpty()) {
          String weapon = inventory.get(randGen.nextInt(inventory.size()));
          attackDoctorLucky(weapon);
          System.out.println(getPlayerName() + " attacks with " + weapon);
        } else {
          System.out.println(getPlayerName() + " has no weapon, using default attack.");
          attackDoctorLucky("default");
        }
        break;
      default:
        // Look around as the default action
        System.out.println(getPlayerName() + " looks around:");
        System.out.println(world.getSpaceInfo(getPlayerLocation().getSpaceName()));
        break;
    }
  }
}
