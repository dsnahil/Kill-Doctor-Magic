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
   * Command to pick up an item in the current space.
   *
   * @param playerName the name of the player issuing the pickup
   * @param itemName   the name of the item to pick up
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

  /** Exposed for GUI logging. */
  public String getItemName() {
    return itemName;
  }
}
