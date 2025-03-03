package controller;

import killdoctorlucky.model.Iworld;

/**
 * Represents a single user command in the Kill Doctor Lucky game.
 */
public interface Icommand {

  /**
   * Executes this command on the given model.
   *
   * @param model the model on which to execute the command
   */
  void execute(Iworld model);

}
