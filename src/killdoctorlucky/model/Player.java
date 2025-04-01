package killdoctorlucky.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the "Kill Doctor Lucky" game. This class manages
 * player attributes such as name, location, and inventory of items.
 */
public class Player implements Iplayer {
  private final String name;
  private Ispace location;
  protected List<Iitem> inventory;
  private final int maxInventory = 5;
  public Iworld world;

  /**
   * Constructs a new Player with the specified name, initial location, and the
   * game world.
   *
   * @param name          The name of the player, used for identification.
   * @param startLocation The starting location of the player within the game
   *                      world.
   * @param world         The game world in which the player exists.
   * @throws IllegalArgumentException if startLocation or world is null
   */
  public Player(String name, Ispace startLocation, Iworld world) {
    if (startLocation == null) {
      throw new IllegalArgumentException("Starting location cannot be null.");
    }
    if (world == null) {
      throw new IllegalArgumentException("World cannot be null for Player.");
    }
    this.name = name;
    this.location = startLocation;
    this.inventory = new ArrayList<>();
    this.world = world;
  }

  @Override
  public String getPlayerName() {
    return name;
  }

  @Override
  public Ispace getPlayerLocation() {
    return location;
  }

  @Override
  public void moveTo(Ispace newSpace) {
    // Only allow a move if the target space is adjacent (in the neighbor list).
    if (!location.getNeighbors().contains(newSpace.getSpaceName())) {
      throw new IllegalArgumentException("Cannot move to a non-adjacent space!");
    }
    this.location = newSpace;
  }

  @Override
  public void removeItem(String itemName) {
    inventory.removeIf(item -> item.getItemName().equalsIgnoreCase(itemName));
  }

  @Override
  public void pickUpItem(String itemName) {
    if (itemName == null || itemName.trim().isEmpty()) {
      throw new IllegalArgumentException("Item name cannot be empty!");
    }
    // Check if the player's inventory is already full
    if (inventory.size() >= maxInventory) {
      throw new IllegalArgumentException("Inventory is full. Cannot pick up more items.");
    }
    // Find the item in the current space
    List<Iitem> spaceItems = ((Space) location).getItemObjects();
    Iitem found = null;
    for (Iitem item : spaceItems) {
      if (item.getItemName().equalsIgnoreCase(itemName)) {
        found = item;
        break;
      }
    }
    if (found == null) {
      throw new IllegalArgumentException("Item not found in this space!");
    }
    inventory.add(found);
    spaceItems.remove(found);
    System.out.println("Picked up: " + found.getItemName());
  }

  @Override
  public void attackDoctorLucky(String weapon) {
    // Must be in the same space as the target.
    if (!location.getSpaceName().equalsIgnoreCase(world.getTargetLocation().getSpaceName())) {
      System.out.println("You must be in the same room as the target to attack!");
      return;
    }
    // Check if any other player sees the attack.
    for (Iplayer other : world.getPlayers()) {
      if (!other.getPlayerName().equalsIgnoreCase(this.name) && world.canPlayerSee(other, this)) {
        System.out.println("Attack was seen by " + other.getPlayerName() + ". No damage dealt!");
        return;
      }
    }
    // If no weapon is available, perform a default 1-damage attack.
    if (inventory.isEmpty() || "default".equalsIgnoreCase(weapon)) {
      System.out.println(name + " pokes the target in the eye (1 damage)!");
      world.setLastAttacker(this.name);
      TargetCharacter.getInstance().decreaseHealth(1);
      if (TargetCharacter.getInstance().getTargetHealth() <= 0) {
        world.setWinner(this.name);
      }
      return;
    }
    // Otherwise, look for the specified weapon in the player's inventory
    for (Iitem item : inventory) {
      if (item.getItemName().equalsIgnoreCase(weapon)) {
        world.setLastAttacker(this.name);
        TargetCharacter.getInstance().decreaseHealth(item.getDamage());
        System.out
            .println(name + " attacked with " + weapon + " for " + item.getDamage() + " damage!");
        // Remove the weapon from inventory after use
        removeItem(item.getItemName());
        if (TargetCharacter.getInstance().getTargetHealth() <= 0) {
          world.setWinner(this.name);
        }
        return;
      }
    }
    System.out.println("Weapon not found in inventory!");
  }

  @Override
  public List<String> getPlayerItems() {
    List<String> itemNames = new ArrayList<>();
    for (Iitem item : inventory) {
      itemNames.add(item.getItemName());
    }
    return itemNames;
  }
}
