package killdoctorlucky.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for World class.
 */
public class WorldTest {

  private World world;

  /**
   * Setup for testing the World.
   */
  @Before
  public void setUp() throws IOException {
    world = new World("res/mansion.txt");
  }

  @Test
  public void testWorldInitialization() {
    assertNotNull(world);
    assertTrue(world.isGameNotOver());
  }

  @Test
  public void testGetSpaceInfoValid() {
    assertNotNull(world.getSpaceInfo(0));
  }

  @Test
  public void testGetSpaceInfoInvalid() {
    assertEquals("Invalid space index", world.getSpaceInfo(-1));
    assertEquals("Invalid space index", world.getSpaceInfo(100));
  }

  @Test
  public void testFindPlayerIndexValid() {
    world.addPlayer("Alice", 0);
    assertEquals(0, world.findPlayerIndex("Alice"));
  }

  @Test
  public void testFindPlayerIndexInvalid() {
    assertEquals(-1, world.findPlayerIndex("Unknown"));
  }

  @Test
  public void testGetPlayersInitiallyEmpty() {
    // A new world should have no players until added.
    assertEquals(0, world.getPlayers().size());
  }

  @Test
  public void testAddPlayer() {
    world.addPlayer("Bob", 0);
    assertEquals(1, world.getPlayers().size());
  }

  @Test
  public void testGetSpaceByNameCaseInsensitive() {
    Ispace space = world.getSpaceByName("armory");
    assertNotNull(space);
    assertEquals("Armory", space.getSpaceName());
  }

  @Test
  public void testGetSpaceInfoString() {
    String info = world.getSpaceInfo("Armory");
    assertTrue(info.contains("Armory"));
    assertTrue(info.contains("Neighbors"));
  }

  @Test
  public void testGenerateWorldMap() {
    BufferedImage map = world.generateWorldMap();
    assertNotNull(map);
  }

  @Test
  public void testGetSpaceItems() {
    List<String> spaceItems = world.getSpaceItems();
    assertTrue(spaceItems.size() > 0);
  }

  @Test
  public void testGetPlayerItemsEmpty() {
    world.addPlayer("TestPlayer", 0);
    List<String> playerItems = world.getPlayerItems();
    assertTrue(playerItems.isEmpty());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddPlayerInvalidSpaceIndex() {
    world.addPlayer("InvalidPlayer", -1);
  }

  @Test
  public void testMoveTargetCharacterCyclesThroughSpaces() {
    Ispace initialTarget = world.getTargetLocation();
    world.moveTargetCharacter();
    Ispace newTarget = world.getTargetLocation();
    if (world.getSpaceInfo(0) != null) {
      assertNotEquals(initialTarget.getSpaceName(), newTarget.getSpaceName());
    }
  }

  @Test
  public void testMoveTargetCharacterCycle() {
    // Move Doctor Lucky through several spaces and ensure a valid target is always
    // returned.
    for (int i = 0; i < 10; i++) {
      world.moveTargetCharacter();
      assertNotNull(world.getTargetLocation());
    }
  }
}
