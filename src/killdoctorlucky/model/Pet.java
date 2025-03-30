package killdoctorlucky.model;

/**
 * Represents the pet associated with the target character in the "Kill Doctor
 * Lucky" game.
 */
public class Pet implements Ipet {
  private String name;
  private Ispace currentSpace;

  /**
   * Constructs a Pet with the specified name and starting space.
   *
   * @param name       the pet's name.
   * @param startSpace the starting space for the pet.
   */
  public Pet(String name, Ispace startSpace) {
    this.name = name;
    this.currentSpace = startSpace;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public Ispace getCurrentSpace() {
    return currentSpace;
  }

  @Override
  public void moveTo(Ispace target) {
    this.currentSpace = target;
  }
}
