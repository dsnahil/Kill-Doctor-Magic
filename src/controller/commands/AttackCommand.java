package controller.commands;

import controller.Icommand;
import java.util.logging.Logger;
import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Iworld;

/**
 * Command for a player to attack Doctor Lucky with a chosen weapon.
 */
public class AttackCommand implements Icommand {
  private final String playerName;
  private final String weaponName;
  private static final Logger logger = Logger.getLogger(AttackCommand.class.getName());

  /**
   * Constructs an AttackCommand.
   *
   * @param playerName the name of the player issuing the attack
   * @param weaponName the name of the weapon used in the attack
   */
  public AttackCommand(String playerName, String weaponName) {
    this.playerName = playerName;
    this.weaponName = weaponName;
  }

  @Override
  public void execute(Iworld model) {
    int pindex = model.findPlayerIndex(playerName);
    if (pindex < 0) {
      throw new IllegalArgumentException("Player not found: " + playerName);
    }
    Iplayer player = model.getPlayers().get(pindex);
    player.attackDoctorLucky(weaponName);
    // Optionally log that the command was executed.
    logger.info(playerName + " executed attack with " + weaponName);
  }
}
