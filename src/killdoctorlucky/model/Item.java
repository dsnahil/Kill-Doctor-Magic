package killdoctorlucky.model;

/**
 * Represents an item in the "Kill Doctor Lucky" game. Each item has a name,
 * damage value, and an associated space index.
 */
public class Item implements Iitem {
  private final String name;
  private final int damage;
  private final int spaceIndex;

  /**
   * Constructs a new Item with specified parameters.
   *
   * @param spaceIndex The index of the space where the item is located.
   * @param damage     The amount of damage this item can inflict when used.
   * @param name       The name of the item.
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
