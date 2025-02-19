package killdoctorlucky.model;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for Player class.
 */
public class PlayerTest {

  private Player player;
  private Space startSpace;
  private Space adjacentSpace;
  private Space nonAdjacentSpace;

  // A dummy world that returns the player's space as the target location.
  private class DummyWorld implements Iworld {
    private Ispace target;

    public DummyWorld(Ispace target) {
      this.target = target;
    }

    @Override
    public Ispace getTargetLocation() {
      return target;
    }

    @Override
    public void setLastAttacker(String name) {
    }

    @Override
    public void setWinner(String name) {
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
    public boolean isGameNotOver() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getWinner() {
      throw new UnsupportedOperationException();
    }

    @Override
    public int findPlayerIndex(String name) {
      throw new UnsupportedOperationException();
    }

    @Override
    public BufferedImage generateWorldMap() {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getSpaceInfo(int i) {
      throw new UnsupportedOperationException();
    }

    @Override
    public String getSpaceInfo(String spaceName) {
      throw new UnsupportedOperationException();
    }

    @Override
    public List<Iplayer> getPlayers() {
      throw new UnsupportedOperationException();
    }

    @Override
    public void addPlayer(String string, int i) {
      throw new UnsupportedOperationException();
    }

    @Override
    public Ispace getSpaceByName(String name) {
      throw new UnsupportedOperationException();
    }

    @Override
    public void moveTargetCharacter() {
      throw new UnsupportedOperationException();
    }
  }

  /**
   * Setup for testing Player actions.
   */
  @Before
  public void setUp() {
    startSpace = new Space(0, 0, 1, 1, "Kitchen");
    adjacentSpace = new Space(1, 1, 2, 2, "Dining Hall");
    nonAdjacentSpace = new Space(5, 5, 6, 6, "Garage");

    // For the valid move test, set "Dining Hall" as neighbor.
    startSpace.getNeighbors().add("Dining Hall");
    player = new Player("Alice", startSpace, new DummyWorld(startSpace));
  }

  @Test
  public void testGetPlayerName() {
    assertEquals("Alice", player.getPlayerName());
  }

  @Test
  public void testGetPlayerLocation() {
    assertEquals("Kitchen", player.getPlayerLocation().getSpaceName());
  }

  @Test
  public void testMoveToValidSpace() {
    player.moveTo(adjacentSpace);
    // Even though we moved the player's location object,
    // the test assumes that the space name reflects the neighbor ("Dining Hall").
    // For testing purposes, we can simulate that by setting the expected name.
    assertEquals("Dining Hall", player.getPlayerLocation().getSpaceName());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMoveToInvalidSpace() {
    player.moveTo(nonAdjacentSpace);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPickUpItemNotInRoom() {
    player.pickUpItem("Nonexistent Item");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPickUpItemWithEmptyName() {
    player.pickUpItem("");
  }

  @Test
  public void testAttackDoctorLuckyWithoutWeapon() {
    // Ensure player and Doctor Lucky are in the same room.
    ((DummyWorld) player.world).target = startSpace;
    TargetCharacter.setInstance("Doctor Lucky", 50);
    TargetCharacter doctorLucky = TargetCharacter.getInstance();
    int initialHealth = doctorLucky.getTargetHealth();

    player.attackDoctorLucky("Loud Noise");
    assertEquals(initialHealth, doctorLucky.getTargetHealth());
  }
}
