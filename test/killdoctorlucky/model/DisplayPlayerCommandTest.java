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

  /**
   * Tests that executing DisplayPlayerCommand for a valid player does not throw
   * an exception.
   */
  @Test
  public void testExecuteDisplayValidPlayer() {
    displayCommand = new DisplayPlayerCommand("MockPlayer");
    try {
      displayCommand.execute(mockWorld);
    } catch (Exception e) {
      fail("DisplayPlayerCommand.execute() threw an exception: " + e.getMessage());
    }
    assertTrue(true);
  }

  /**
   * Tests that executing DisplayPlayerCommand for an unknown player throws an
   * exception.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testExecuteDisplayUnknownPlayer() {
    displayCommand = new DisplayPlayerCommand("Unknown");
    displayCommand.execute(mockWorld);
  }

  // Minimal mock implementation of Iworld.
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

    // For display, we can stub getSpaceInfo, though it won't be used here.
    @Override
    public String getSpaceInfo(String spaceName) {
      return "Space: " + spaceName;
    }

    @Override
    public String getSpaceInfo(int index) {
      return null;
    }

    // The rest are stubs:
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
  }

  // Minimal mock implementation of Iplayer.
  private class MockPlayer implements Iplayer {
    private String name;
    private Ispace location;

    public MockPlayer(String name) {
      this.name = name;
      // Provide a valid location using our minimal mock space.
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

  // Minimal mock implementation of Ispace.
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

    // The rest are stubs:
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
      return Collections.emptyList();
    }
  }
}
