package controller;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import killdoctorlucky.model.Ipet;
import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Ispace;
import killdoctorlucky.model.Iworld;
import org.junit.Before;
import org.junit.Test;
import view.GameView;

/**
 * Tests that clicking an inventory item invokes an attack and advances the
 * turn.
 */
public class GuiControllerHandleInventoryClickTest {

  private TestWorld world;
  private TestView view;
  private GuiController controller;

  /**
   * Prepare a fresh controller and stubs before each test.
   */
  @Before
  public void setUp() throws IOException {
    view = new TestView();
    world = new TestWorld();
    controller = new GuiController(world, view, 5);
  }

  /**
   * Clicking on an inventory item should attack with that item and consume a
   * turn.
   */
  @Test
  public void testHandleInventoryClick_attacksAndAdvancesTurn() {
    controller.handleInventoryClick("Knife");

    assertEquals("Knife", world.player.recordedWeapon);
    assertEquals(1, world.moveCount);
  }

  /** Minimal world stub that records moves and delegates to a TestPlayer. */
  private static class TestWorld implements Iworld {
    int moveCount = 0;
    final TestPlayer player = new TestPlayer("P");
    final List<Iplayer> players = Collections.singletonList(player);

    @Override
    public List<Iplayer> getPlayers() {
      return players;
    }

    @Override
    public int findPlayerIndex(String n) {
      return 0;
    }

    @Override
    public void moveTargetCharacter() {
      moveCount++;
    }

    @Override
    public boolean isGameNotOver() {
      return true;
    }

    @Override
    public List<String> getPlayerItems() {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getSpaceItems() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void addPlayer(String n, int i) {
      throw new UnsupportedOperationException();
    }

    @Override
    public java.awt.image.BufferedImage generateWorldMap() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Ispace getTargetLocation() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String viewTargetCharacter() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Ispace getSpaceByName(String n) {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getSpaceInfo(String s) {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getSpaceInfo(int i) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setWinner(String n) {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getWinner() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setLastAttacker(String n) {
      throw new UnsupportedOperationException();
    }

    @Override
    public Ipet getPet() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean canPlayerSee(Iplayer a, Iplayer b) {
      throw new UnsupportedOperationException();
    }
  }

  /** Minimal player stub that records the attacked weapon. */
  private static class TestPlayer implements Iplayer {
    final String name;
    String recordedWeapon = null;

    TestPlayer(String n) {
      name = n;
    }

    @Override
    public String getPlayerName() {
      return name;
    }

    @Override
    public Ispace getPlayerLocation() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void moveTo(Ispace s) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void removeItem(String i) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void pickUpItem(String i) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void attackDoctorLucky(String w) {
      recordedWeapon = w;
    }

    @Override
    public List<String> getPlayerItems() {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * A lightweight GameView stub to swallow UI updates.
   */
  private static class TestView extends GameView {
    private static final long serialVersionUID = 1L;

    public TestView() throws java.io.IOException {
      super();
    }

    @Override
    public void appendToLog(String text) {
      /* ignore */
    }

    @Override
    public void redrawMap(java.awt.image.BufferedImage img) {
      /* ignore */
    }

    @Override
    public void setEntities(List<Iplayer> p, Ispace t) {
      /* ignore */
    }

    @Override
    public void setStatusText(String s) {
      /* ignore */
    }
  }
}
