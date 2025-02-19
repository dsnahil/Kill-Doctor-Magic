package killdoctorlucky.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a space on the "Kill Doctor Lucky" game board. Each space is
 * defined by its position on the grid (upper and lower bounds) and can contain
 * items.
 */
public class Space implements Ispace {
  private int upperRow;
  private int upperCol;
  private int lowerRow;
  private int lowerCol;
  private String name;
  private List<Iitem> items;
  private List<String> neighbors;

  /**
   * Constructs a new Space with specified boundaries and name.
   *
   * @param upperRow the upper row boundary of the space on the game board.
   * @param upperCol the upper column boundary of the space.
   * @param lowerRow the lower row boundary of the space on the game board.
   * @param lowerCol the lower column boundary of the space.
   * @param name     the name of the space, used for identification.
   */

  public Space(int upperRow, int upperCol, int lowerRow, int lowerCol, String name) {
    this.upperRow = upperRow;
    this.upperCol = upperCol;
    this.lowerRow = lowerRow;
    this.lowerCol = lowerCol;
    this.name = name;
    this.items = new ArrayList<>();
    this.neighbors = new ArrayList<>();
  }

  public List<Iitem> getItemObjects() {
    return items;
  }

  @Override
  public int getUpperRow() {
    return upperRow;
  }

  @Override
  public int getUpperColumn() {
    return upperCol;
  }

  @Override
  public int getLowerRow() {
    return lowerRow;
  }

  @Override
  public int getLowerColumn() {
    return lowerCol;
  }

  @Override
  public String getSpaceName() {
    return name;
  }

  /**
   * Adds an item to the space's inventory.
   *
   * @param itemName the name of the item to be added to the space. This item will
   *                 be added to the list of items in this space.
   */
  public void addItem(String itemName) {
    addItem(new Item(-1, 0, itemName));
  }

  @Override
  public void addItem(Iitem item) {
    // TODO Auto-generated method stub

  }

  @Override
  public List<String> getItems() {
    List<String> itemNames = new ArrayList<>();
    for (Iitem item : items) {
      itemNames.add(item.getItemName());
    }
    return itemNames;
  }

  @Override
  public List<String> getNeighbors() {
    return neighbors;
  }

  /**
   * Adds a neighboring space to this space's list of neighbors. This method
   * establishes a relationship between the current space and its adjacent space.
   *
   * @param neighborName the name of the neighboring space to be added. This space
   *                     will be considered a neighbor of the current space.
   */
  public void addNeighbor(String neighborName) {
    neighbors.add(neighborName);
  }

}
