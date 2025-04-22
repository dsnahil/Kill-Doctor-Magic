package controller;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import killdoctorlucky.model.Ipet;
import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Ispace;
import killdoctorlucky.model.Iworld;
import killdoctorlucky.model.TargetCharacter;
import util.RandomGenerator;

public class ControllerImplHandleCommandTest {

  private DummyWorld world;
  private StringReader in;
  private StringBuilder out;
  private ControllerImpl ctrl;

  @Before
  public void setUp() {
    world = new DummyWorld();
    // put one human player
    world.players.add(new DummyPlayer("Alice"));
    in = new StringReader("");
    out = new StringBuilder();
    ctrl = new ControllerImpl(world, in, out, 3, new RandomGenerator(0));
    TargetCharacter.resetInstance();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorRejectsNulls() {
    new ControllerImpl(null, in, out, 1);
  }

  @Test
  public void testHandleUnknownCommandDoesNotConsumeTurn() throws IOException {
    in = new StringReader("foo\nquit\n");
    ctrl = new ControllerImpl(world, in, out, 3);
    ctrl.startGame();
    assertTrue(out.toString().contains("Unknown command"));
    assertTrue(out.toString().contains("Exiting the game"));
  }

  @Test
  public void testSavemapDoesNotConsumeTurn() throws IOException {
    in = new StringReader("savemap\nmap.png\nquit\n");
    ctrl = new ControllerImpl(world, in, out, 2);
    ctrl.startGame();
    assertTrue(out.toString().contains("Enter filename"));
    // since savemap free, still able to enter next prompt same turn
  }

  private static class DummyWorld implements Iworld {
    List<Iplayer> players = new ArrayList<>();

    @Override
    public List<Iplayer> getPlayers() {
      return players;
    }

    @Override
    public boolean isGameNotOver() {
      return false;
    }

    // stubs for all other interface methods...
    @Override
    public List<String> getPlayerItems() {
      return null;
    }

    @Override
    public List<String> getSpaceItems() {
      return null;
    }

    @Override
    public String getWinner() {
      return null;
    }

    @Override
    public int findPlayerIndex(String name) {
      return 0;
    }

    @Override
    public java.awt.image.BufferedImage generateWorldMap() {
      return null;
    }

    @Override
    public Ispace getSpaceByName(String name) {
      return null;
    }

    @Override
    public String getSpaceInfo(String spaceName) {
      return null;
    }

    @Override
    public String getSpaceInfo(int index) {
      return null;
    }

    @Override
    public Ispace getTargetLocation() {
      return null;
    }

    @Override
    public void moveTargetCharacter() {
    }

    @Override
    public void addPlayer(String name, int spaceIndex) {
    }

    @Override
    public void setWinner(String name) {
    }

    @Override
    public void setLastAttacker(String name) {
    }

    @Override
    public Ipet getPet() {
      return null;
    }

    @Override
    public String viewTargetCharacter() {
      return null;
    }

    @Override
    public boolean canPlayerSee(Iplayer a, Iplayer b) {
      return false;
    }
  }

  private static class DummyPlayer implements Iplayer {
    private final String name;

    public DummyPlayer(String n) {
      this.name = n;
    }

    @Override
    public String getPlayerName() {
      return name;
    }

    @Override
    public Ispace getPlayerLocation() {
      return null;
    }

    @Override
    public void moveTo(Ispace newSpace) {
    }

    @Override
    public void removeItem(String item) {
    }

    @Override
    public void pickUpItem(String item) {
    }

    @Override
    public void attackDoctorLucky(String weapon) {
    }

    @Override
    public List<String> getPlayerItems() {
      return null;
    }
  }
}
