package controller.commands;

import java.util.logging.Logger;
import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Iworld;

/**
 * Command for a player to attack the target using a chosen weapon.
 */
public class AttackCommand implements Icommand {
  private static final Logger logger = Logger.getLogger(AttackCommand.class.getName());

  private final String playerName;
  private final String weaponName;

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
    logger.info(playerName + " executed attack with " + weaponName);
  }

  /**
   * Gets the name of the weapon used in the attack.
   *
   * @return the weapon name.
   */
  public String getWeaponName() {
    return weaponName;
  }
}
