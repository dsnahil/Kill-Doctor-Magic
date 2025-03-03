package controller.commands;

import controller.Icommand;
import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Ispace;
import killdoctorlucky.model.Iworld;

/**
 * Command for a player to "look around" their current space.
 */
public class LookCommand implements Icommand {
  private final String playerName;

  public LookCommand(String playerName) {
    this.playerName = playerName;
  }

  @Override
  public void execute(Iworld model) {
    int pIndex = model.findPlayerIndex(playerName);
    if (pIndex < 0) {
      throw new IllegalArgumentException("Player not found: " + playerName);
    }
    Iplayer player = model.getPlayers().get(pIndex);
    Ispace currentSpace = player.getPlayerLocation();

    // Example: just print the space info to console or store it for output
    System.out.println(model.getSpaceInfo(currentSpace.getSpaceName()));
  }
}
