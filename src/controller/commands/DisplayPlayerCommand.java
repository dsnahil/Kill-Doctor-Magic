package controller.commands;

import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Iworld;

/**
 * Command to display a player's details (location, inventory, etc.).
 */
public class DisplayPlayerCommand implements Icommand {
  private final String playerName;

  /**
   * Constructs a DisplayPlayerCommand for a specific player.
   *
   * @param playerName the name of the player whose details are to be displayed.
   */
  public DisplayPlayerCommand(String playerName) {
    this.playerName = playerName;
  }

  @Override
  public void execute(Iworld model) {
    int pindex = model.findPlayerIndex(playerName);
    if (pindex < 0) {
      throw new IllegalArgumentException("Player not found: " + playerName);
    }
    Iplayer player = model.getPlayers().get(pindex);

    System.out.println("Player: " + player.getPlayerName());
    System.out.println("Location: " + player.getPlayerLocation().getSpaceName());
    System.out.println("Items: " + player.getPlayerItems());
  }
}
