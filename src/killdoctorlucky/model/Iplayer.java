package killdoctorlucky.model;

import java.util.List;

/**
 * Represents a player in the "Kill Doctor Lucky" game.
 */
public interface Iplayer {

  /**
   * Retrieves the name of the player.
   *
   * @return The player's name as a String.
   */
  String getPlayerName();

  /**
   * Retrieves the current location of the player within the game space.
   *
   * @return The current space where the player is located.
   */
  Ispace getPlayerLocation();

  /**
   * Moves the player to a new specified space.
   *
   * @param newSpace The new space to which the player should move.
   * @throws IllegalArgumentException if the space is not adjacent
   */
  void moveTo(Ispace newSpace);

  /**
   * Removes an item from the player's inventory by name.
   *
   * @param item The name of the item to remove.
   */
  void removeItem(String item);

  /**
   * Adds (picks up) an item to the player's inventory by name.
   *
   * @param item The name of the item to pick up.
   * @throws IllegalArgumentException if the item is not found in the current
   *                                  space
   */
  void pickUpItem(String item);

  /**
   * Attacks the target character using a specified weapon from the player's
   * inventory. If the attack is seen by another player, it fails. If the player
   * has no items, a default "poke in the eye" attack for 1 damage is used.
   *
   * @param weapon The name of the weapon used in the attack.
   */
  void attackDoctorLucky(String weapon);

  /**
   * Retrieves a list of item names in the player's inventory.
   *
   * @return A list of item names the player currently holds.
   */
  List<String> getPlayerItems();
}
