package controller.commands;

import controller.Icommand;
import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Iworld;

/**
 * Command to pick up an item in the current space.
 */
public class PickupCommand implements Icommand {
  private final String playerName;
  private final String itemName;

  public PickupCommand(String playerName, String itemName) {
    this.playerName = playerName;
    this.itemName = itemName;
  }

  @Override
  public void execute(Iworld model) {
    int pIndex = model.findPlayerIndex(playerName);
    if (pIndex < 0) {
      throw new IllegalArgumentException("Player not found: " + playerName);
    }
    Iplayer player = model.getPlayers().get(pIndex);
    player.pickUpItem(itemName);
  }
}
