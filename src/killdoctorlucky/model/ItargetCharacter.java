package killdoctorlucky.model;

/**
 * Represents the target character, Doctor Lucky, in the "Kill Doctor Lucky"
 * game. This interface defines the necessary actions and properties related to
 * Doctor Lucky's health and identification.
 */
public interface ItargetCharacter {

  /**
   * Retrieves the current health of the target.
   *
   * @return The current health as an integer value.
   */
  int getTargetHealth();

  /**
   * Gets the name of the target.
   *
   * @return The name of the target as a String.
   */
  String getTargetName();

  /**
   * Decreases the health of the target by a specified amount.
   *
   * @param damage The amount of health to decrease.
   */
  void decreaseHealth(int damage);
}
