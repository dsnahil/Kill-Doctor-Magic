package killdoctorlucky.model;

import static org.junit.Assert.assertTrue;

import controller.commands.Icommand;
import controller.commands.LookCommand;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for LookCommand in isolation using a minimal mock Iworld.
 */
public class LookCommandTest {

  private Icommand lookCommand;
  private Iworld mockWorld;

  /**
   * Sets up the test fixture by initializing a new MockWorld instance.
   */
  @Before
  public void setUp() {
    mockWorld = new MockWorld();
  }

  @Test
  public void testExecuteLook() {
    lookCommand = new LookCommand("MockPlayer");
    // Simply calling execute; if an exception occurs the test will fail.
    lookCommand.execute(mockWorld);
    assertTrue(true);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExecuteLookPlayerNotFound() {
    Icommand badCommand = new LookCommand("Unknown");
    badCommand.execute(mockWorld);
  }

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
      // no-op for testing
    }

    @Override
    public boolean getHasPet() {
      return false;
    }
  }
}
