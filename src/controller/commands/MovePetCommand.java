package controller.commands;

import controller.Icommand;
import killdoctorlucky.model.Ipet;
import killdoctorlucky.model.Ispace;
import killdoctorlucky.model.Iworld;
import killdoctorlucky.model.Space;

/**
 * Command to move the target character's pet to a specified space.
 */
public class MovePetCommand implements Icommand {
  private final String targetSpaceName;

  /**
   * Constructs a MovePetCommand.
   *
   * @param targetSpaceName the name of the space to which the pet should be
   *                        moved.
   */
  public MovePetCommand(String targetSpaceName) {
    this.targetSpaceName = targetSpaceName;
  }

  @Override
  public void execute(Iworld model) {
    Ispace newSpace = model.getSpaceByName(targetSpaceName);
    Ipet pet = model.getPet();
    if (pet == null) {
      throw new IllegalArgumentException("No pet found in the game.");
    }
    // Remove pet flag from the current space.
    if (pet.getCurrentSpace() != null && pet.getCurrentSpace() instanceof Space) {
      ((Space) pet.getCurrentSpace()).setHasPet(false);
    }
    pet.moveTo(newSpace);
    if (newSpace instanceof Space) {
      ((Space) newSpace).setHasPet(true);
    }
    System.out.println("Pet moved to " + newSpace.getSpaceName());
  }
}
