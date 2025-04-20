package controller;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
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
 * Tests that handleMoveTo causes the player to move and advances the turn.
 */
public class GuiControllerHandleMoveToTest {

  private TestWorld world;
  private TestView view;
  private GuiController controller;

  /**
   * Prepare a fresh controller and stubs before each test.
   */
  @Before
  public void setUp() throws Exception {
    view = new TestView();
    world = new TestWorld();
    controller = new GuiController(world, view, 5);
  }

  /**
   * Invoking handleMoveTo should move the current player and then advance Doctor
   * Lucky.
   */
  @Test
  public void testHandleMoveTo_executesMoveAndAdvancesTurn() {
    controller.handleMoveTo("Library");

    assertEquals("Library", world.player.lastMove);
    assertEquals(1, world.moveCount);
  }

  /** Minimal world stub that returns a TestSpace and records turn advancement. */
  private static class TestWorld implements Iworld {
    int moveCount = 0;
    final TestSpace targetLocation = new TestSpace("Armory");
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
    public Ispace getSpaceByName(String n) {
      return new TestSpace(n);
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
    public BufferedImage generateWorldMap() {
      throw new UnsupportedOperationException();
    }

    @Override
    public Ispace getTargetLocation() {
      return targetLocation;
    }

    @Override
    public String viewTargetCharacter() {
      return "";
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

  /** Minimal player stub that records the name of the last moved-to space. */
  private static class TestPlayer implements Iplayer {
    final String name;
    String lastMove = null;

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
      lastMove = s.getSpaceName();
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
      throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getPlayerItems() {
      throw new UnsupportedOperationException();
    }
  }

  /** Minimal space stub that holds only a name. */
  private static class TestSpace implements Ispace {
    private final String name;

    TestSpace(String n) {
      name = n;
    }

    @Override
    public String getSpaceName() {
      return name;
    }

    @Override
    public List<String> getNeighbors() {
      return Collections.emptyList();
    }

    @Override
    public int getUpperRow() {
      return 0;
    }

    @Override
    public int getUpperColumn() {
      return 0;
    }

    @Override
    public int getLowerRow() {
      return 0;
    }

    @Override
    public int getLowerColumn() {
      return 0;
    }

    @Override
    public void addItem(killdoctorlucky.model.Iitem i) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getItems() {
      return Collections.emptyList();
    }

    @Override
    public void setHasPet(boolean f) {
      // no-op
    }

    @Override
    public boolean getHasPet() {
      return false;
    }
  }

  /**
   * Stubbed GameView that ignores UI calls.
   */
  private static class TestView extends GameView {
    private static final long serialVersionUID = 1L;

    public TestView() throws java.io.IOException {
      super();
    }

    @Override
    public void appendToLog(String t) {
      // ignore
    }

    @Override
    public void redrawMap(BufferedImage m) {
      // ignore
    }

    @Override
    public void setEntities(List<Iplayer> p, Ispace t) {
      // ignore
    }

    @Override
    public void setStatusText(String s) {
      // ignore
    }
  }
}
