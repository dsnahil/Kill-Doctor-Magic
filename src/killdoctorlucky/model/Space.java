package killdoctorlucky.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a space on the "Kill Doctor Lucky" game board. Each space is
 * defined by its position on the grid and can contain items.
 */
public class Space implements Ispace {
  private int upperRow;
  private int upperCol;
  private int lowerRow;
  private int lowerCol;
  private String name;
  private List<Iitem> items;
  private List<String> neighbors;
  private boolean hasPet; // indicates if the pet is in this space

  /**
   * Constructs a new Space with specified boundaries and name.
   *
   * @param upperRow the upper row boundary of the space on the game board
   * @param upperCol the upper column boundary of the space
   * @param lowerRow the lower row boundary of the space on the game board
   * @param lowerCol the lower column boundary of the space
   * @param name     the name of the space
   */
  public Space(int upperRow, int upperCol, int lowerRow, int lowerCol, String name) {
    this.upperRow = upperRow;
    this.upperCol = upperCol;
    this.lowerRow = lowerRow;
    this.lowerCol = lowerCol;
    this.name = name;
    this.items = new ArrayList<>();
    this.neighbors = new ArrayList<>();
    this.hasPet = false;
  }

  /**
   * Copy constructor to create a defensive copy of an existing Space.
   *
   * @param other the space to copy
   */
  public Space(Space other) {
    this.upperRow = other.upperRow;
    this.upperCol = other.upperCol;
    this.lowerRow = other.lowerRow;
    this.lowerCol = other.lowerCol;
    this.name = other.name;
    this.items = new ArrayList<>(other.items);
    this.neighbors = new ArrayList<>(other.neighbors);
    this.hasPet = other.hasPet;
  }

  /**
   * Returns the internal list of item objects (package-private).
   *
   * @return the list of Iitem objects in this space
   */
  List<Iitem> getItemObjects() {
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

  @Override
  public void addItem(Iitem item) {
    items.add(item);
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
   * Adds a neighboring space to this space's list of neighbors. (package-private
   * for internal use)
   *
   * @param neighborName the name of the neighboring space
   */
  void addNeighbor(String neighborName) {
    neighbors.add(neighborName);
  }

  @Override
  public void setHasPet(boolean flag) {
    this.hasPet = flag;
  }

  @Override
  public boolean getHasPet() {
    return this.hasPet;
  }
}
