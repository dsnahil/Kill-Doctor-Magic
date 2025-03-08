package killdoctorlucky.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the "Kill Doctor Lucky" game. This class manages
 * player attributes such as name, location, and inventory of items.
 */
public class Player implements Iplayer {
  public Iworld world;
  private final String name;
  private Ispace location;
  private final List<Iitem> inventory;
  private final int maxInventory = 5;

  /**
   * Constructs a new Player with the specified name, initial location, and the
   * game world.
   *
   * @param name          The name of the player, used for identification.
   * @param startLocation The starting location of the player within the game
   *                      world.
   * @param world         The game world in which the player exists. This is used
   *                      to interact with the game environment.
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
    // Allow move only if newSpace is a neighbor of the current location
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
    // Check if the player is in the same room as Doctor Lucky
    if (!location.getSpaceName().equalsIgnoreCase(world.getTargetLocation().getSpaceName())) {
      System.out.println("You must be in the same room as Doctor Lucky to attack!");
      return;
    }

    // Look for the weapon in the player's inventory
    for (Iitem item : inventory) {
      if (item.getItemName().equalsIgnoreCase(weapon)) {
        // Record this player as the last attacker
        world.setLastAttacker(this.name);
        // Attack and decrease Doctor Lucky's health
        TargetCharacter.getInstance().decreaseHealth(item.getDamage());
        System.out.println(name + " attacked Doctor Lucky with " + weapon + "!");
        // Remove the weapon from the inventory after the attack
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
