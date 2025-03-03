package controller.commands;

import controller.Icommand;
import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Iworld;

/**
 * Command to display a player's details (location, inventory, etc.).
 */
public class DisplayPlayerCommand implements Icommand {
  private final String playerName;

  public DisplayPlayerCommand(String playerName) {
    this.playerName = playerName;
  }

  @Override
  public void execute(Iworld model) {
    int pIndex = model.findPlayerIndex(playerName);
    if (pIndex < 0) {
      throw new IllegalArgumentException("Player not found: " + playerName);
    }
    Iplayer player = model.getPlayers().get(pIndex);

    System.out.println("Player: " + player.getPlayerName());
    System.out.println("Location: " + player.getPlayerLocation().getSpaceName());
    System.out.println("Items: " + player.getPlayerItems());
  }
}
