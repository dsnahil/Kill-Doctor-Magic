package view;

/**
 * Controller callbacks from the GUI.
 */
public interface IviewFeatures {
  /** Handle loading a new world file. */
  void handleNewWorld();

  /** Handle starting a new game. */
  void handleNewGame();

  /** Handle quitting the application. */
  void handleQuit();

  /** Handle advancing to the next turn. */
  void handleNextTurn();

  /** Handle a move action (via Move button or key 'M'). */
  void handleMove();

  /** Handle moving to a specific space (via map click). */
  void handleMoveTo(String spaceName);

  /** Handle picking up an item (via button or key 'P'). */
  void handlePickup();

  /** Handle looking at a space (via button or key 'L'). */
  void handleLook();

  /** Handle an attack action (via button or key 'A'). */
  void handleAttack();

  /** Handle describing the current player. */
  void handleDescribe();

  /** Handle saving the current map to a file. */
  void handleSaveMap();

  /** Double‑click an inventory item to attack with it. */
  void handleInventoryClick(String itemName);

  /** Handle moving the pet character. */
  void handleMovePet();

  /** Handle clicking on the map at the given coordinates. */
  void handleMapClick(int x, int y);
}
