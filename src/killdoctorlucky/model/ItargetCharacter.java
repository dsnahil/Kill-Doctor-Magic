package killdoctorlucky.model;

/**
 * Represents the target character, Doctor Lucky, in the "Kill Doctor Lucky"
 * game. This interface defines the necessary actions and properties related to
 * Doctor Lucky's health and identification.
 */
public interface ItargetCharacter {

  /**
   * Retrieves the current health of Doctor Lucky.
   *
   * @return The current health as an integer value.
   */
  int getTargetHealth();

  /**
   * Gets the name of Doctor Lucky.
   *
   * @return The name of Doctor Lucky as a String.
   */
  String getTargetName();

  /**
   * Decreases the health of Doctor Lucky by a specified amount. This method is
   * typically called when Doctor Lucky is attacked.
   *
   * @param damage The amount of health to decrease, represented as an integer.
   */
  void decreaseHealth(int damage);
}
