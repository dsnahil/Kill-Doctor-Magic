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
   * it attacks using the weapon with the most damage. Otherwise, it prioritizes:
   * 1. Picking up items if available. 2. Moving toward Doctor Lucky's location if
   * possible. 3. Looking around as a fallback.
   *
   * @return a string describing the action taken
   */
  public String takeTurn() {
    Ispace current = getPlayerLocation();
    Ispace targetLocation = world.getTargetLocation();

    // Check if in same room as target and unseen
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
        for (Iitem item : inventory) {
          if (item.getDamage() > bestDamage) {
            bestDamage = item.getDamage();
            bestWeapon = item.getItemName();
          }
        }
        attackDoctorLucky(bestWeapon);
        return "attacked Doctor Lucky with " + bestWeapon;
      }
    }

    // Priority: Pickup > Move toward target > Look
    List<String> items = getPlayerLocation().getItems();
    if (!items.isEmpty()) {
      String item = items.get(randGen.nextInt(items.size()));
      pickUpItem(item);
      return "picked up " + item;
    }

    List<String> neighbors = getPlayerLocation().getNeighbors();
    if (!neighbors.isEmpty()) {
      // Try to move toward Doctor Lucky's location
      String targetSpace = chooseSpaceTowardTarget(neighbors);
      moveTo(world.getSpaceByName(targetSpace));
      return "moved to " + targetSpace;
    }

    // Fallback: look around
    return "looked around: " + world.getSpaceInfo(getPlayerLocation().getSpaceName());
  }

  /**
   * Chooses a neighboring space that moves closer to Doctor Lucky's location.
   * Falls back to a random neighbor if no clear path is better.
   *
   * @param neighbors list of neighboring space names
   * @return the chosen space name
   */
  private String chooseSpaceTowardTarget(List<String> neighbors) {
    Ispace targetLocation = world.getTargetLocation();
    String bestSpace = neighbors.get(randGen.nextInt(neighbors.size())); // Default random
    int minDistance = Integer.MAX_VALUE;

    for (String neighborName : neighbors) {
      Ispace neighbor = world.getSpaceByName(neighborName);
      int distance = estimateDistance(neighbor, targetLocation);
      if (distance < minDistance) {
        minDistance = distance;
        bestSpace = neighborName;
      }
    }
    return bestSpace;
  }

  /**
   * Estimates the Manhattan distance between two spaces based on their
   * coordinates.
   *
   * @param space1 first space
   * @param space2 second space
   * @return estimated distance
   */
  private int estimateDistance(Ispace space1, Ispace space2) {
    int x1 = (space1.getUpperColumn() + space1.getLowerColumn()) / 2;
    int y1 = (space1.getUpperRow() + space1.getLowerRow()) / 2;
    int x2 = (space2.getUpperColumn() + space2.getLowerColumn()) / 2;
    int y2 = (space2.getUpperRow() + space2.getLowerRow()) / 2;
    return Math.abs(x1 - x2) + Math.abs(y1 - y2);
  }
}