package killdoctorlucky.model;

import java.util.List;

/**
 * Represents a space within the "Kill Doctor Lucky" game board. This interface
 * defines the properties and behaviors that each space must support.
 */
public interface Ispace {

  /**
   * Gets the upper row number of the space.
   *
   * @return the upper row index of the space.
   */
  int getUpperRow();

  /**
   * Gets the upper column number of the space.
   *
   * @return the upper column index of the space.
   */
  int getUpperColumn();

  /**
   * Gets the lower row number of the space.
   *
   * @return the lower row index of the space.
   */
  int getLowerRow();

  /**
   * Gets the lower column number of the space.
   *
   * @return the lower column index of the space.
   */
  int getLowerColumn();

  /**
   * Retrieves the name of the space.
   *
   * @return the name of the space as a String.
   */
  String getSpaceName();

  /**
   * Adds an item to the space.
   *
   * @param item the item to add to this space.
   */
  void addItem(Iitem item);

  /**
   * Retrieves a list of item names currently in the space.
   *
   * @return a list of Strings representing the names of the items in this space.
   */
  List<String> getItems();

  /**
   * Retrieves a list of neighboring spaces' names.
   *
   * @return a list of Strings representing the names of neighboring spaces.
   */
  List<String> getNeighbors();
}
