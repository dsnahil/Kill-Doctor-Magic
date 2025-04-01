package killdoctorlucky.model;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the canPlayerSee method and verifies that when a neighboring room.
 * contains a pet, the room’s description marks that neighbor as “Not visible.”
 */
public class CanPlayerSeeTest {

  private DummyWorld world;
  private Space room1;
  private Space room2;
  private Player playerA;
  private Player playerB;

  /**
   * Sets up a simple two-room world with neighbors and players for testing
   * visibility.
   *
   * @throws IOException if there is an error during world creation
   */
  @Before
  public void setUp() throws IOException {
    room1 = new Space(0, 0, 1, 1, "Room1");
    room2 = new Space(0, 1, 1, 2, "Room2");
    room1.getNeighbors().add("Room2");
    room2.getNeighbors().add("Room1");
    world = new DummyWorld(Arrays.asList(room1, room2));
    playerA = new Player("A", room1, world);
    playerB = new Player("B", room2, world);
    world.getPlayers().addAll(Arrays.asList(playerA, playerB));
  }

  @Test
  public void testCanPlayerSeeNeighbor() {
    assertTrue("Player A should be able to see Player B in a neighboring room",
        world.canPlayerSee(playerA, playerB));
  }

  @Test
  public void testNeighborVisibilityWithPet() {
    // Place pet in room2.
    room2.setHasPet(true);
    String info = world.getSpaceInfo("Room1");
    assertTrue("Room1 description should mark Room2 as not visible when pet is present",
        info.contains("Room2 (Not visible)"));
  }

  private static class DummyWorld extends World {
    public DummyWorld(java.util.List<Ispace> spaces) throws IOException {
      super("dummy");
      this.spaces.clear();
      this.spaces.addAll(spaces);
      this.players.clear();
    }

    @Override
    public java.util.List<Iplayer> getPlayers() {
      return players;
    }
  }
}
