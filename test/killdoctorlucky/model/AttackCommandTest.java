package killdoctorlucky.model;

import static org.junit.Assert.assertEquals;

import controller.Icommand;
import controller.commands.AttackCommand;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for AttackCommand in isolation using a minimal mock Iworld.
 */
public class AttackCommandTest {

  private Icommand attackCommand;
  private Iworld mockWorld;

  /**
   * Sets up the minimal mock world for testing AttackCommand.
   */
  @Before
  public void setUp() {
    mockWorld = new MockWorld();
    // Reset Doctor Lucky
    TargetCharacter.setInstance("Doctor Lucky", 50);
  }

  @Test
  public void testExecuteValidAttack() {
    // The player has "Sword" in inventory and is in the same space as Doctor Lucky
    attackCommand = new AttackCommand("MockPlayer", "Sword");
    attackCommand.execute(mockWorld);

    // Check if Doctor Lucky's HP decreased from 50 -> 40
    assertEquals(40, TargetCharacter.getInstance().getTargetHealth());
  }

  @Test
  public void testExecuteNoWeaponInInventory() {
    // The player does not have "Gun" in inventory
    attackCommand = new AttackCommand("MockPlayer", "Gun");
    attackCommand.execute(mockWorld);

    // Confirm HP is still 50
    assertEquals(50, TargetCharacter.getInstance().getTargetHealth());
  }

  /**
   * Minimal mock Iworld for testing AttackCommand.
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
    public String getSpaceInfo(String spaceName) {
      return null;
    }

    @Override
    public String getSpaceInfo(int index) {
      return null;
    }

    @Override
    public Ispace getSpaceByName(String name) {
      return null;
    }

    @Override
    public Ispace getTargetLocation() {
      return null; // pretend the player is in the same space as Doctor Lucky
    }

    @Override
    public void moveTargetCharacter() {
    }

    @Override
    public java.awt.image.BufferedImage generateWorldMap() {
      return null;
    }

    // New stubs to satisfy updated Iworld interface:
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
   * Minimal mock player for testing AttackCommand.
   */
  private class MockPlayer implements Iplayer {
    private String name;
    private List<String> inventory;

    public MockPlayer(String name) {
      this.name = name;
      // Has "Sword" in inventory
      this.inventory = new ArrayList<>();
      inventory.add("Sword");
    }

    @Override
    public String getPlayerName() {
      return name;
    }

    @Override
    public Ispace getPlayerLocation() {
      // We'll pretend the player is always in the same space as Doctor Lucky
      return null;
    }

    @Override
    public void moveTo(Ispace newSpace) {
    }

    @Override
    public void removeItem(String item) {
      inventory.remove(item);
    }

    @Override
    public void pickUpItem(String item) {
    }

    @Override
    public void attackDoctorLucky(String weapon) {
      // If "Sword," do 10 damage
      if (!inventory.contains(weapon)) {
        System.out.println("Weapon not found in inventory!");
        return;
      }
      removeItem(weapon);
      TargetCharacter.getInstance().decreaseHealth(10);
      System.out.println(name + " attacked Doctor Lucky with " + weapon + "!");
    }

    @Override
    public List<String> getPlayerItems() {
      return inventory;
    }
  }
}
