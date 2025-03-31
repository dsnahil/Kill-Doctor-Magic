package killdoctorlucky.model;

import java.util.logging.Logger;

/**
 * Represents the target character, often "Doctor Lucky", in the "Kill Doctor
 * Lucky" game. Implemented as a singleton so only one instance exists.
 */
public class TargetCharacter implements ItargetCharacter {
  private static final Logger logger = Logger.getLogger(TargetCharacter.class.getName());

  private static TargetCharacter instance;
  private final String name;
  private int health;

  /**
   * Private constructor to enforce singleton usage.
   *
   * @param name   the name of the target character
   * @param health the initial health of the target
   */
  TargetCharacter(String name, int health) {
    this.name = name;
    this.health = health;
  }

  /**
   * Provides access to the singleton instance of the TargetCharacter. If the
   * instance does not exist, it initializes with default values.
   *
   * @return the single instance of TargetCharacter
   */
  public static TargetCharacter getInstance() {
    if (instance == null) {
      instance = new TargetCharacter("Doctor Lucky", 50);
    }
    return instance;
  }

  /**
   * Initializes or updates the singleton instance of the TargetCharacter.
   *
   * @param name   the name of the target
   * @param health the new health value of the target
   */
  public static void setInstance(String name, int health) {
    if (instance == null) {
      instance = new TargetCharacter(name, health);
    } else {
      instance.health = health;
    }
  }

  /**
   * Resets the singleton instance, allowing a new game to start fresh.
   */
  public static void resetInstance() {
    instance = null;
  }

  @Override
  public int getTargetHealth() {
    return health;
  }

  @Override
  public String getTargetName() {
    return name;
  }

  @Override
  public void decreaseHealth(int damage) {
    if (health > 0) {
      health -= damage;
      if (health < 0) {
        health = 0;
      }
      logger.info(name + " now has " + health + " HP left.");
    }
  }
}
