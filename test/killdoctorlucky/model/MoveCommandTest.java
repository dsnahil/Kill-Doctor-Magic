package killdoctorlucky.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import controller.Icommand;
import controller.commands.MoveCommand;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for MoveCommand in isolation using a minimal mock Iworld.
 */
public class MoveCommandTest {

  private Icommand moveCommand;
  private Iworld mockWorld;

  /**
   * Sets up the minimal mock world for testing MoveCommand.
   */
  @Before
  public void setUp() {
    // Create a minimal mock world
    mockWorld = new MockWorld();
  }

  @Test
  public void testExecuteValidMove() {
    // Player is at "MockSpace" which has a neighbor "NeighborSpace"
    moveCommand = new MoveCommand("MockPlayer", "NeighborSpace");
    moveCommand.execute(mockWorld);
    // If no exception is thrown, the move was valid.
    // Verify that the player's location is now "NeighborSpace".
    MockWorld w = (MockWorld) mockWorld;
    assertEquals("NeighborSpace", ((MockPlayer) w.mockPlayer).location.getSpaceName());
  }

  @Test
  public void testExecuteInvalidMove() {
    // Attempt moving to a space that isn't a neighbor
    moveCommand = new MoveCommand("MockPlayer", "NonNeighbor");
    try {
      moveCommand.execute(mockWorld);
      fail("Expected an IllegalArgumentException for invalid move");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Cannot move to a non-adjacent space!"));
    }
  }

  /**
   * Minimal mock world that has exactly one player ("MockPlayer") in "MockSpace,"
   * which has exactly one neighbor: "NeighborSpace". Also implements
   * getSpaceByName.
   */
  private class MockWorld implements Iworld {
    private Iplayer mockPlayer;

    public MockWorld() {
      mockPlayer = new MockPlayer("MockPlayer");
    }

    @Override
    public int findPlayerIndex(String name) {
      if ("MockPlayer".equalsIgnoreCase(name)) {
        return 0;
      }
      return -1;
    }

    @Override
    public List<Iplayer> getPlayers() {
      return Collections.singletonList(mockPlayer);
    }

    @Override
    public Ispace getSpaceByName(String name) {
      // For valid neighbor, return a new MockSpace with that name.
      if ("NeighborSpace".equalsIgnoreCase(name) || "NonNeighbor".equalsIgnoreCase(name)) {
        return new MockSpace(name);
      }
      throw new UnsupportedOperationException("getSpaceByName not implemented for name: " + name);
    }

    // The rest of these methods can be stubbed or throw
    // UnsupportedOperationException if not used.
    @Override
    public void addPlayer(String name, int spaceIndex) {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getWinner() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setWinner(String name) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void setLastAttacker(String name) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean isGameNotOver() {
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
    public String getSpaceInfo(String spaceName) {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getSpaceInfo(int index) {
      throw new UnsupportedOperationException();
    }

    @Override
    public Ispace getTargetLocation() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void moveTargetCharacter() {
      throw new UnsupportedOperationException();
    }

    @Override
    public java.awt.image.BufferedImage generateWorldMap() {
      throw new UnsupportedOperationException();
    }
  }

  private class MockPlayer implements Iplayer {
    private String name;
    private MockSpace location;

    public MockPlayer(String name) {
      this.name = name;
      // The player's starting space is "MockSpace" with a single neighbor
      // "NeighborSpace"
      this.location = new MockSpace("MockSpace");
      this.location.addNeighbor("NeighborSpace");
    }

    @Override
    public String getPlayerName() {
      return name;
    }

    @Override
    public Ispace getPlayerLocation() {
      return location;
    }

    @Override
    public void moveTo(Ispace newSpace) {
      // Check if newSpace is a neighbor; if not, throw exception.
      if (!location.getNeighbors().contains(newSpace.getSpaceName())) {
        throw new IllegalArgumentException("Cannot move to a non-adjacent space!");
      }
      this.location = (MockSpace) newSpace;
    }

    @Override
    public void removeItem(String item) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void pickUpItem(String item) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void attackDoctorLucky(String weapon) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getPlayerItems() {
      return new ArrayList<>();
    }
  }

  private class MockSpace implements Ispace {
    private String spaceName;
    private List<String> neighbors;

    public MockSpace(String name) {
      this.spaceName = name;
      this.neighbors = new ArrayList<>();
    }

    public void addNeighbor(String neighbor) {
      neighbors.add(neighbor);
    }

    @Override
    public String getSpaceName() {
      return spaceName;
    }

    @Override
    public List<String> getNeighbors() {
      return neighbors;
    }

    // The remaining methods are stubs:
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
    public void addItem(Iitem item) {
    }

    @Override
    public List<String> getItems() {
      return new ArrayList<>();
    }
  }
}
