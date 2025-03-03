package killdoctorlucky.model;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for player attacking Doctor Lucky.
 */
public class PlayerAttackTest {
  private Player player;
  private TargetCharacter doctorLucky;

  /**
   * A simple dummy world that only supports the needed methods for attack tests.
   */
  private class DummyWorld implements Iworld {
    private Ispace targetSpace;

    public DummyWorld(Ispace targetSpace) {
      this.targetSpace = targetSpace;
    }

    @Override
    public Ispace getTargetLocation() {
      return targetSpace;
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
   * Setup for testing attacks.
   */
  @Before
  public void setUp() {
    // For these tests, use a space named "Parlor" as the player's starting
    // location.
    Space parlor = new Space(0, 0, 1, 1, "Parlor");
    // In some tests we want Doctor Lucky to be in the same room, so we use parlor.
    DummyWorld dummyWorldSameRoom = new DummyWorld(parlor);
    player = new Player("Alice", parlor, dummyWorldSameRoom);
    // Reset Doctor Lucky's health
    TargetCharacter.setInstance("Doctor Lucky", 50);
    doctorLucky = TargetCharacter.getInstance();
  }

  @Test
  public void testAttackDoctorLuckyWithoutWeapon() {
    int initialHealth = doctorLucky.getTargetHealth();
    player.attackDoctorLucky("Loud Noise");
    assertEquals(initialHealth, doctorLucky.getTargetHealth());
  }

  @Test
  public void testAttackWithNonexistentWeapon() {
    // Use a dummy world where player and target are in the same room.
    Space room = new Space(0, 0, 1, 1, "Parlor");
    DummyWorld dummyWorldSameRoom = new DummyWorld(room);
    Player localPlayer = new Player("Alice", room, dummyWorldSameRoom);
    int initialHealth = doctorLucky.getTargetHealth();
    localPlayer.attackDoctorLucky("Nonexistent Weapon");
    assertEquals(initialHealth, doctorLucky.getTargetHealth());
  }
}
