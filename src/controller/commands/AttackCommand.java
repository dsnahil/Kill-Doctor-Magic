package controller.commands;

import controller.Icommand;
import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Iworld;

/**
 * Command for a player to attack Doctor Lucky with a chosen weapon.
 */
public class AttackCommand implements Icommand {
  private final String playerName;
  private final String weaponName;

  public AttackCommand(String playerName, String weaponName) {
    this.playerName = playerName;
    this.weaponName = weaponName;
  }

  @Override
  public void execute(Iworld model) {
    int pIndex = model.findPlayerIndex(playerName);
    if (pIndex < 0) {
      throw new IllegalArgumentException("Player not found: " + playerName);
    }
    Iplayer player = model.getPlayers().get(pIndex);
    player.attackDoctorLucky(weaponName);
  }
}
