package controller.commands;

import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Ispace;
import killdoctorlucky.model.Iworld;

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
    int pindex = model.findPlayerIndex(playerName);
    if (pindex < 0) {
      throw new IllegalArgumentException("Player not found: " + playerName);
    }
    Iplayer player = model.getPlayers().get(pindex);
    Ispace newSpace = model.getSpaceByName(targetSpaceName);
    player.moveTo(newSpace); // Will throw if invalid neighbor
  }
}
