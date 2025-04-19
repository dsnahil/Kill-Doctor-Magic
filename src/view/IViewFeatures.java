package view;

/**
 * Controller callbacks from the GUI.
 */
public interface IViewFeatures {
  void handleNewWorld();

  void handleNewGame();

  void handleQuit();

  void handleNextTurn();

  void handleMove(); // via Move button or key 'M'

  void handleMoveTo(String spaceName); // via map‑click

  void handlePickup(); // via button or key 'P'

  void handleLook(); // via button or key 'L'

  void handleAttack(); // via button or key 'A'

  void handleDescribe();

  void handleSaveMap();

  void handleMovePet();

  void handleMapClick(int x, int y);
}
