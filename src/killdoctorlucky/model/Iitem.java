package killdoctorlucky.model;

/**
 * Represents an item in the "Kill Doctor Lucky" game. Defines the necessary
 * methods that each item must implement.
 */
public interface Iitem {

  /**
   * Gets the name of the item.
   *
   * @return The name of the item as a String.
   */
  String getItemName();

  /**
   * Gets the damage value of the item. This value represents how much damage the
   * item can inflict.
   *
   * @return The damage value as an integer.
   */
  int getDamage();

  /**
   * Gets the index of the space where the item is located.
   *
   * @return The index of the space in the game as an integer.
   */
  int getSpaceIndex();
}
