package killdoctorlucky.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import controller.commands.Icommand;
import controller.commands.PickupCommand;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for PickupCommand in isolation using a minimal mock Iworld.
 */
public class PickupCommandTest {

  private Icommand pickupCommand;
  private Iworld mockWorld;

  /**
   * Sets up the test fixture by initializing a new MockWorld instance.
   */
  @Before
  public void setUp() {
    mockWorld = new MockWorld();
  }

  @Test
  public void testExecuteValidPickup() {
    pickupCommand = new PickupCommand("MockPlayer", "TestItem");
    pickupCommand.execute(mockWorld);
    MockWorld w = (MockWorld) mockWorld;
    assertTrue(w.mockPlayer.getPlayerItems().contains("TestItem"));
  }

  @Test
  public void testExecuteInvalidPickup() {
    pickupCommand = new PickupCommand("MockPlayer", "Nonexistent");
    try {
      pickupCommand.execute(mockWorld);
      fail("Expected IllegalArgumentException for item not found");
    } catch (IllegalArgumentException e) {
      assertTrue(e.getMessage().contains("Item not found in this space!"));
    }
  }

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
    public Ispace getSpaceByName(String name) {
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
    public BufferedImage generateWorldMap() {
      throw new UnsupportedOperationException();
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
    private MockSpace location;
    private List<String> inventory = new ArrayList<>();

    public MockPlayer(String name) {
      this.name = name;
      this.location = new MockSpace("MockSpace");
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
      throw new UnsupportedOperationException();
    }

    @Override
    public void removeItem(String item) {
      inventory.remove(item);
    }

    @Override
    public void pickUpItem(String itemName) {
      if (!location.items.contains(itemName)) {
        throw new IllegalArgumentException("Item not found in this space!");
      }
      inventory.add(itemName);
      location.items.remove(itemName);
    }

    @Override
    public void attackDoctorLucky(String weapon) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getPlayerItems() {
      return inventory;
    }
  }

  private class MockSpace implements Ispace {
    private String spaceName;
    private List<String> items;

    public MockSpace(String name) {
      this.spaceName = name;
      this.items = new ArrayList<>();
      items.add("TestItem");
    }

    @Override
    public String getSpaceName() {
      return spaceName;
    }

    @Override
    public List<String> getNeighbors() {
      return new ArrayList<>();
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
    }

    @Override
    public List<String> getItems() {
      return items;
    }

    // New methods:
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
