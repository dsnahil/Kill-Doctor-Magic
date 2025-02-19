package killdoctorlucky.model;

/**
 * Represents an item in the "Kill Doctor Lucky" game. Each item has a name,
 * damage value, and an associated space index indicating where it can be found.
 */
public class Item implements Iitem {
  private final String name;
  private final int damage;
  private final int spaceIndex;

  /**
   * Constructs a new Item with specified parameters.
   *
   * @param spaceIndex The index of the space where the item is located. This
   *                   helps in identifying the item's position on the game board.
   * @param damage     The amount of damage this item can inflict when used. This
   *                   value is critical in game mechanics, particularly during
   *                   attacks.
   * @param name       The name of the item. This is used to identify the item
   *                   within the game.
   */
  public Item(int spaceIndex, int damage, String name) {
    this.spaceIndex = spaceIndex;
    this.damage = damage;
    this.name = name;
  }

  @Override
  public String getItemName() {
    return name;
  }

  @Override
  public int getDamage() {
    return damage;
  }

  @Override
  public int getSpaceIndex() {
    return spaceIndex;
  }

  @Override
  public String toString() {
    return name + " (Damage: " + damage + ")";
  }
}
