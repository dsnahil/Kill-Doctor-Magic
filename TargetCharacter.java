package killdoctorlucky.model;

/**
 * Represents the target character, Doctor Lucky, in the "Kill Doctor Lucky"
 * game. This class is implemented as a singleton to ensure that only one
 * instance of Doctor Lucky exists throughout the game.
 */
public class TargetCharacter implements ItargetCharacter {
  private static TargetCharacter instance;
  private final String name;
  private int health;

  /**
   * Private constructor to enforce singleton usage. Initializes a new
   * TargetCharacter with a name and health.
   *
   * @param name   the name of the character, typically "Doctor Lucky".
   * @param health the initial health of Doctor Lucky.
   */
  TargetCharacter(String name, int health) {
    this.name = name;
    this.health = health;
  }

  /**
   * Provides access to the singleton instance of the TargetCharacter. If the
   * instance does not exist, it initializes it with default values.
   *
   * @return the single instance of TargetCharacter.
   */
  public static TargetCharacter getInstance() {
    if (instance == null) {
      instance = new TargetCharacter("Doctor Lucky", 50);
    }
    return instance;
  }

  /**
   * Initializes or updates the singleton instance of the TargetCharacter. This
   * method can also reset the instance with new values if needed.
   *
   * @param name   the new name of Doctor Lucky, if resetting the instance.
   * @param health the new health value of Doctor Lucky, if resetting the
   *               instance.
   */
  public static void setInstance(String name, int health) {
    if (instance == null) {
      instance = new TargetCharacter(name, health);
    } else {
      instance.health = health;
    }
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
      System.out.println(name + " now has " + health + " HP left.");
    }
  }
}
