package killdoctorlucky.model;

/**
 * Represents the pet associated with the target character in the "Kill Doctor
 * Lucky" game.
 */
public interface Ipet {

  /**
   * Gets the name of the pet.
   *
   * @return the pet's name
   */
  String getName();

  /**
   * Gets the current space where the pet is located.
   *
   * @return the current Ispace object
   */
  Ispace getCurrentSpace();

  /**
   * Moves the pet to a specified space.
   *
   * @param target the space to which the pet should move
   */
  void moveTo(Ispace target);
}
