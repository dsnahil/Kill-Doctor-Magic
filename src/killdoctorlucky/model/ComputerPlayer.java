package killdoctorlucky.model;

import java.util.List;

/**
 * A simple computer-controlled player that randomly chooses an action each
 * turn.
 */
public class ComputerPlayer extends Player {

  public ComputerPlayer(String name, Ispace startLocation, Iworld world) {
    super(name, startLocation, world);
  }

  // Potentially override or add AI logic here, e.g. choose random items to pick
  // up, etc.
}
