package controller.commands;

import killdoctorlucky.model.Iworld;
import controller.Icommand;
import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Ispace;

/**
 * Command to move a specific player to a neighboring space.
 */
public class MoveCommand implements Icommand {
  private final String playerName;
  private final String targetSpaceName;

  /**
   * Constructs a MoveCommand.
   * 
   * @param playerName      the name of the player who is moving
   * @param targetSpaceName the name of the space to move to
   */
  public MoveCommand(String playerName, String targetSpaceName) {
    this.playerName = playerName;
    this.targetSpaceName = targetSpaceName;
  }

  @Override
  public void execute(Iworld model) {
    int pIndex = model.findPlayerIndex(playerName);
    if (pIndex < 0) {
      throw new IllegalArgumentException("Player not found: " + playerName);
    }
    Iplayer player = model.getPlayers().get(pIndex);
    Ispace newSpace = model.getSpaceByName(targetSpaceName);
    player.moveTo(newSpace); // Will throw if invalid neighbor
  }
}
