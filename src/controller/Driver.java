package controller;

import killdoctorlucky.model.ComputerPlayer;
import killdoctorlucky.model.Iworld;
import killdoctorlucky.model.World;

import java.io.IOException;
import java.util.Scanner;

/**
 * Entry point for launching the Kill Doctor Lucky game with a synchronous
 * controller.
 */
public class Driver {

  public static void main(String[] args) {
    // 1. Parse command-line arguments
    String filePath = "res/mansion.txt";
    int maxTurns = 10;
    System.out.println("Hello");
    if (args.length > 0) {
      filePath = args[0]; // first argument is the file
    }
    if (args.length > 1) {
      maxTurns = Integer.parseInt(args[1]); // second argument is max turns
    }

    // 2. Instantiate the model
    Iworld model;
    try {
      model = new World(filePath);
    } catch (IOException e) {
      System.out.println("Failed to load world: " + e.getMessage());
      return;
    }

    // 3. Create the controller with System.in and System.out
    Icontroller controller = new ControllerImpl(model, System.in, System.out, maxTurns);

    // 4. Add some players to the game (example)
    model.addPlayer("Alice", 0); // human
    
    ComputerPlayer cpu = new ComputerPlayer("CPU", model.getSpaceByName("Kitchen"), model);
    model.getPlayers().add(cpu);

    // 5. Start the game
    try {
      controller.startGame();
    } catch (IOException e) {
      System.out.println("I/O Error in controller: " + e.getMessage());
    }
  }
}
