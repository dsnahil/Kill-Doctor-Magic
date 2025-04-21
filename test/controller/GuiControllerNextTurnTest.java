package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Ispace;
import killdoctorlucky.model.Iworld;
import org.junit.Before;
import org.junit.Test;
import view.GameView;

/**
 * Tests that handleNextTurn advances the game when not over and redraws the
 * view.
 */
public class GuiControllerNextTurnTest {

  private TestWorld world;
  private TestView view;
  private GuiController controller;

  /**
   * Sets up a fresh controller, stub world, and stub view before each test.
   */
  @Before
  public void setUp() throws IOException {
    view = new TestView();
    world = new TestWorld();
    controller = new GuiController(world, view, 5);
  }

  /**
   * When the game is in progress, handleNextTurn should advance Doctor Lucky, log
   * the turn, redraw the map, entities, and update status text.
   */
  @Test
  public void testHandleNextTurn_advancesAndRedraws() {
    world.gameNotOver = true;
    controller.handleNextTurn();

    assertEquals(0, world.moveCount);
    assertEquals(1, view.logs.size());
    assertTrue(view.logs.get(0).startsWith("--- After Turn 1"));
    assertNotNull(view.lastMap);
    assertSame(world.players, view.lastPlayers);
    assertSame(world.targetLocation, view.lastTarget);
    assertTrue(view.lastStatus.contains("Turn 1"));
  }

  /**
   * When the game is over or max turns reached, handleNextTurn should log that
   * the game is over without advancing Doctor Lucky.
   */
  @Test
  public void testHandleNextTurn_gameOverOrMax() {
    world.gameNotOver = false;
    controller.handleNextTurn();
    assertTrue(view.logs.contains("Game Over or max turns reached."));
  }

  // --- stubs below ---

  private static class TestWorld implements Iworld {
    boolean gameNotOver = true;
    int moveCount = 0;
    final Ispace targetLocation = new TestSpace("Armory");
    final List<Iplayer> players = Collections.singletonList(new TestPlayer("P", targetLocation));

    @Override
    public List<Iplayer> getPlayers() {
      return players;
    }

    @Override
    public boolean isGameNotOver() {
      return gameNotOver;
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
      return "Doctor Lucky at " + targetLocation.getSpaceName();
    }

    // other methods unused...
    @Override
    public int findPlayerIndex(String n) {
      throw new UnsupportedOperationException();
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
    public killdoctorlucky.model.Ipet getPet() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean canPlayerSee(Iplayer a, Iplayer b) {
      throw new UnsupportedOperationException();
    }
  }

  private static class TestPlayer implements Iplayer {
    private final String name;
    private final Ispace loc;

    TestPlayer(String n, Ispace l) {
      name = n;
      loc = l;
    }

    @Override
    public String getPlayerName() {
      return name;
    }

    @Override
    public Ispace getPlayerLocation() {
      return loc;
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
   * Stubbed GameView that records calls for verification.
   */
  private static class TestView extends GameView {
    private static final long serialVersionUID = 1L;

    List<String> logs = new java.util.ArrayList<>();
    BufferedImage lastMap;
    List<Iplayer> lastPlayers;
    Ispace lastTarget;
    String lastStatus;

    public TestView() throws java.io.IOException {
      super();
    }

    @Override
    public void appendToLog(String text) {
      logs.add(text);
    }

    @Override
    public void redrawMap(BufferedImage map) {
      lastMap = map;
    }

    @Override
    public void setEntities(List<Iplayer> players, Ispace target) {
      lastPlayers = players;
      lastTarget = target;
    }

    @Override
    public void setStatusText(String text) {
      lastStatus = text;
    }
  }
}
