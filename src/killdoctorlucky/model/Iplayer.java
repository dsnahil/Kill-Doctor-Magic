package killdoctorlucky.model;

import java.util.List;

/**
 * Represents a player in the "Kill Doctor Lucky" game. This interface defines
 * the necessary actions a player can perform.
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
   */
  void moveTo(Ispace newSpace);

  /**
   * Removes an item from the player's inventory.
   *
   * @param item The name of the item to remove.
   */
  void removeItem(String item);

  /**
   * Adds an item to the player's inventory.
   *
   * @param item The name of the item to pick up.
   */
  void pickUpItem(String item);

  /**
   * Attacks Doctor Lucky using a specified weapon from the player's inventory.
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
