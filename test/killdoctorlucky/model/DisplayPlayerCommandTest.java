package killdoctorlucky.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import controller.Icommand;
import controller.commands.DisplayPlayerCommand;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for DisplayPlayerCommand in isolation using a minimal mock Iworld.
 */
public class DisplayPlayerCommandTest {

  private Icommand displayCommand;
  private Iworld mockWorld;

  /**
   * Sets up the minimal mock world for testing DisplayPlayerCommand.
   */
  @Before
  public void setUp() {
    mockWorld = new MockWorld();
  }

  @Test
  public void testExecuteDisplayValidPlayer() {
    displayCommand = new DisplayPlayerCommand("MockPlayer");
    try {
      displayCommand.execute(mockWorld);
    } catch (IllegalArgumentException e) {
      fail("DisplayPlayerCommand.execute() threw an IllegalArgumentException: " + e.getMessage());
    }
    // If no exception is thrown, the test passes.
    assertTrue(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExecuteDisplayUnknownPlayer() {
    displayCommand = new DisplayPlayerCommand("Unknown");
    displayCommand.execute(mockWorld);
  }

  /**
   * Minimal mock Iworld for testing DisplayPlayerCommand.
   */
  private class MockWorld implements Iworld {
    private Iplayer mockPlayer = new MockPlayer("MockPlayer");

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
    public String getSpaceInfo(String spaceName) {
      return "Space: " + spaceName;
    }

    @Override
    public String getSpaceInfo(int index) {
      return null;
    }

    @Override
    public void addPlayer(String name, int spaceIndex) {
    }

    @Override
    public String getWinner() {
      return null;
    }

    @Override
    public void setWinner(String name) {
    }

    @Override
    public void setLastAttacker(String name) {
    }

    @Override
    public boolean isGameNotOver() {
      return false;
    }

    @Override
    public List<String> getPlayerItems() {
      return null;
    }

    @Override
    public List<String> getSpaceItems() {
      return null;
    }

    @Override
    public Ispace getSpaceByName(String name) {
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
    public java.awt.image.BufferedImage generateWorldMap() {
      return null;
    }

    // New stubs:
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

  /**
   * Minimal mock player for testing.
   */
  private class MockPlayer implements Iplayer {
    private String name;
    private Ispace location;

    public MockPlayer(String name) {
      this.name = name;
      this.location = new MockSpace("MockLocation");
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
      return Collections.emptyList();
    }
  }

  /**
   * Minimal mock space for testing.
   */
  private class MockSpace implements Ispace {
    private String spaceName;

    public MockSpace(String name) {
      this.spaceName = name;
    }

    @Override
    public String getSpaceName() {
      return spaceName;
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
    public void addItem(Iitem item) {
      // Stub
    }

    @Override
    public List<String> getItems() {
      return Collections.emptyList();
    }

    // New methods required by Ispace:
    @Override
    public void setHasPet(boolean flag) {
      // For testing, this can be a no-op.
    }

    @Override
    public boolean getHasPet() {
      return false;
    }
  }
}
