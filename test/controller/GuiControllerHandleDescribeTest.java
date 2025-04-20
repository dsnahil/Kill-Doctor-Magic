package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

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
 * Tests for GuiController.handleDescribe behavior.
 */
public class GuiControllerHandleDescribeTest {

  private TestWorld world;
  private TestView view;
  private GuiController controller;

  /**
   * Prepare test fixtures before each test.
   */
  @Before
  public void setUp() throws Exception {
    view = new TestView();
    world = new TestWorld();
    controller = new GuiController(world, view, 5);
  }

  @Test
  public void testHandleDescribe_redrawsWithoutAdvancingTurn() {
    controller.handleDescribe();

    // describe should not have advanced the turn
    assertEquals(0, world.moveCount);

    // but it must redraw
    assertNotNull(view.lastMap);
    assertSame(world.players, view.lastPlayers);
    assertSame(world.targetLocation, view.lastTarget);
    assertNotNull(view.lastStatus);
  }

  /** Minimal stub world for describe tests. */
  private static class TestWorld implements Iworld {
    int moveCount = 0;
    final Ispace targetLocation = new TestSpace("Foyer");
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
    public BufferedImage generateWorldMap() {
      return new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
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
    public boolean isGameNotOver() {
      return true;
    }

    // unused stubs...
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

  /** Minimal stub player for describe tests. */
  private static class TestPlayer implements Iplayer {
    final String name;

    TestPlayer(String n) {
      name = n;
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
      throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getPlayerItems() {
      throw new UnsupportedOperationException();
    }
  }

  /** Minimal stub space for describe tests. */
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
      /* no-op */
    }

    @Override
    public boolean getHasPet() {
      return false;
    }
  }

  /**
   * A lightweight View stub to capture redraw calls.
   */
  private static class TestView extends GameView {
    private static final long serialVersionUID = 1L;

    BufferedImage lastMap;
    List<Iplayer> lastPlayers;
    Ispace lastTarget;
    String lastStatus;

    public TestView() throws java.io.IOException {
      super();
    }

    @Override
    public void appendToLog(String text) {
      /* ignore */
    }

    @Override
    public void redrawMap(BufferedImage m) {
      lastMap = m;
    }

    @Override
    public void setEntities(List<Iplayer> p, Ispace t) {
      lastPlayers = p;
      lastTarget = t;
    }

    @Override
    public void setStatusText(String s) {
      lastStatus = s;
    }
  }
}
