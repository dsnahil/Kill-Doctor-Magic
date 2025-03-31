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
   * to control randomness for testing or real gameplay.
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
   * If the computer is in the same space as the target and is unseen by others,
   * it chooses deterministically to attack using the weapon in its inventory that
   * does the most damage. Otherwise, it falls back to a random action.
   */
  public void takeTurn() {
    Ispace current = getPlayerLocation();
    Ispace targetLocation = world.getTargetLocation();
    // Check if in same room as target
    if (current.getSpaceName().equalsIgnoreCase(targetLocation.getSpaceName())) {
      boolean isSeen = false;
      for (Iplayer other : world.getPlayers()) {
        if (!other.getPlayerName().equalsIgnoreCase(getPlayerName())
            && world.canPlayerSee(other, this)) {
          isSeen = true;
          break;
        }
      }
      if (!isSeen) {
        // Determine the weapon with the highest damage
        int bestDamage = 0;
        String bestWeapon = "default";
        // Access the protected inventory from the parent Player class.
        for (Iitem item : inventory) {
          if (item.getDamage() > bestDamage) {
            bestDamage = item.getDamage();
            bestWeapon = item.getItemName();
          }
        }
        // Attack using best weapon if available, otherwise default.
        attackDoctorLucky(bestWeapon);
        System.out.println(getPlayerName() + " attacks with " + bestWeapon);
        return;
      }
    }
    // Fallback: choose a random action.
    int choice = randGen.nextInt(4);
    switch (choice) {
      case 0:
        // Move: choose a random neighbor if available.
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
        // Pickup: choose a random item from current space, if any.
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
        // Attack: use a random weapon from inventory, or default if none.
        List<String> inventoryList = getPlayerItems();
        if (!inventoryList.isEmpty()) {
          String weapon = inventoryList.get(randGen.nextInt(inventoryList.size()));
          attackDoctorLucky(weapon);
          System.out.println(getPlayerName() + " attacks with " + weapon);
        } else {
          System.out.println(getPlayerName() + " has no weapon, using default attack.");
          attackDoctorLucky("default");
        }
        break;
      default:
        // Look around.
        System.out.println(getPlayerName() + " looks around:");
        System.out.println(world.getSpaceInfo(getPlayerLocation().getSpaceName()));
        break;
    }
  }
}
