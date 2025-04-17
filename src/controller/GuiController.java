// File: src/controller/GuiController.java
package controller;

import controller.commands.AttackCommand;
import controller.commands.MoveCommand;
import controller.commands.MovePetCommand;
import controller.commands.PickupCommand;
import controller.commands.SaveMapCommand;
import killdoctorlucky.model.Iworld;
import util.RandomGenerator;
import view.IView;
import view.IViewFeatures;

import javax.swing.*;
import java.io.IOException;

public class GuiController implements Icontroller, IViewFeatures {

  private final Iworld model;
  private final IView view;
  private final RandomGenerator randGen;
  private final int maxTurns;
  private int turnCount = 0;

  public GuiController(Iworld model, IView view, int maxTurns) {
    if (model == null || view == null) {
      throw new IllegalArgumentException("Model and view cannot be null.");
    }
    this.model = model;
    this.view = view;
    this.randGen = new RandomGenerator();
    this.maxTurns = maxTurns;

    view.appendToLog("Welcome to Kill Doctor Lucky (GUI)!");
    redrawAll();
  }

  private void redrawAll() {
    view.redrawMap(model.generateWorldMap());
    view.appendToLog("Target: " + model.viewTargetCharacter());
  }

  @Override
  public void startGame() throws IOException {
    // Not used in GUI mode
  }

  @Override
  public void handleNextTurn() {
    if (model.isGameNotOver() && turnCount < maxTurns) {
      model.moveTargetCharacter();
      // Extra credit: wandering pet
      if (model instanceof killdoctorlucky.model.World) {
        ((killdoctorlucky.model.World) model).movePetAutomatically();
      }
      turnCount++;
      redrawAll();
    } else {
      view.appendToLog("Game over or max turns reached.");
    }
  }

  @Override
  public void handleMove() {
    exec(() -> new MoveCommand(getPlayer(), prompt("Enter destination space")).execute(model));
  }

  @Override
  public void handlePickup() {
    exec(() -> new PickupCommand(getPlayer(), prompt("Enter item to pick up")).execute(model));
  }

  @Override
  public void handleLook() {
    exec(() -> view.appendToLog(model.getSpaceInfo(prompt("Enter space to look at"))));
  }

  @Override
  public void handleAttack() {
    exec(
        () -> new AttackCommand(getPlayer(), prompt("Enter weapon to attack with")).execute(model));
  }

  @Override
  public void handleDescribe() {
    exec(() -> view.appendToLog("You: " + model.getPlayers().get(0).getPlayerName()));
  }

  @Override
  public void handleSaveMap() {
    exec(() -> new SaveMapCommand(prompt("Enter filename (e.g. map.png)")).execute(model));
  }

  @Override
  public void handleMovePet() {
    exec(() -> new MovePetCommand(prompt("Enter space to move pet to")).execute(model));
  }

  private void exec(Runnable action) {
    try {
      action.run();
    } catch (Exception e) {
      view.appendToLog("Error: " + e.getMessage());
    }
    // always refresh map & status
    redrawAll();
  }

  private String prompt(String message) {
    String s = JOptionPane.showInputDialog(message + ":");
    return (s == null) ? "" : s.trim();
  }

  private String getPlayer() {
    // assume first player is human
    return model.getPlayers().get(0).getPlayerName();
  }
}
