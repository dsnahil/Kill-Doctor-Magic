package killdoctorlucky.model;

import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Represents the world or game board in the "Kill Doctor Lucky" game. This
 * interface defines all functionalities to interact with the game world,
 * including player/space management, game state checks, and visualization.
 */
public interface Iworld {

  /**
   * Retrieves a list of items currently held by all players.
   *
   * @return A list of strings representing the names of the items.
   */
  List<String> getPlayerItems();

  /**
   * Retrieves a list of items from all spaces in the game world.
   *
   * @return A list of strings representing the items in each space.
   */
  List<String> getSpaceItems();

  /**
   * Checks if the game is still ongoing.
   *
   * @return true if the game is not over, false otherwise.
   */
  boolean isGameNotOver();

  /**
   * Gets the name of the player who won the game.
   *
   * @return The winner's name if there is one, otherwise "No one".
   */
  String getWinner();

  /**
   * Finds the index of a player based on their name.
   *
   * @param name The name of the player to find.
   * @return The index of the player or -1 if not found.
   */
  int findPlayerIndex(String name);

  /**
   * Generates a visual representation of the game world as an image.
   *
   * @return A BufferedImage representing the current state of the game world.
   */
  BufferedImage generateWorldMap();

  /**
   * Retrieves a list of all players currently in the game.
   *
   * @return A list of Iplayer objects representing the players.
   */
  List<Iplayer> getPlayers();

  /**
   * Adds a new player to the game at a specified starting space.
   *
   * @param name       The name of the player.
   * @param spaceIndex The index of the starting space for the new player.
   */
  void addPlayer(String name, int spaceIndex);

  /**
   * Retrieves a specific space by its name.
   *
   * @param name The name of the space to retrieve.
   * @return The Ispace object representing the specified space.
   */
  Ispace getSpaceByName(String name);

  /**
   * Retrieves detailed information about a space based on its name.
   *
   * @param spaceName The name of the space.
   * @return A string containing detailed information about the space.
   */
  String getSpaceInfo(String spaceName);

  /**
   * Retrieves detailed information about a space based on its index.
   *
   * @param index The index of the space.
   * @return A string containing detailed information about the space.
   */
  String getSpaceInfo(int index);

  /**
   * Retrieves the current location of the target character in the game world.
   *
   * @return The Ispace object where the target character is located.
   */
  Ispace getTargetLocation();

  /**
   * Moves the target character to the next space in a predefined sequence or
   * logic.
   */
  void moveTargetCharacter();

  /**
   * Sets the winner of the game based on the last player who successfully
   * attacked the target.
   *
   * @param name The name of the player who won the game.
   */
  void setWinner(String name);

  /**
   * Records the name of the last player who attacked the target character.
   *
   * @param name The name of the player.
   */
  void setLastAttacker(String name);

  /**
   * Returns a concise description of the target character, e.g., "Doctor Lucky
   * (50 HP) at Armory".
   *
   * @return a string describing the target
   */
  String viewTargetCharacter();

  /**
   * Retrieves the pet associated with the target character.
   *
   * @return the IPet object representing the pet
   */
  Ipet getPet();

  /**
   * Determines if one player (pA) can see another (pB). Typically, a player can
   * see another if they are in the same space or in a neighboring space.
   *
   * @param a the observing player
   * @param b the player to be observed
   * @return true if pA can see pB, false otherwise
   */
  boolean canPlayerSee(Iplayer a, Iplayer b);
}
