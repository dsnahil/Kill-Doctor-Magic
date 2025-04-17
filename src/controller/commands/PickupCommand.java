package controller.commands;

import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Iworld;

/**
 * Command to pick up an item in the current space.
 */
public class PickupCommand implements Icommand {
  private final String playerName;
  private final String itemName;

  /**
   * Command to pick up an item in the current space. This command is used to
   * request that a player picks up a specific item from the space in which they
   * are currently located.
   */
  public PickupCommand(String playerName, String itemName) {
    this.playerName = playerName;
    this.itemName = itemName;
  }

  @Override
  public void execute(Iworld model) {
    int pindex = model.findPlayerIndex(playerName);
    if (pindex < 0) {
      throw new IllegalArgumentException("Player not found: " + playerName);
    }
    Iplayer player = model.getPlayers().get(pindex);
    player.pickUpItem(itemName);
  }
}
