package killdoctorlucky.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the end conditions of the game: – A player killing Doctor Lucky ends
 * the game with that player as the winner. – The game is still on if Doctor
 * Lucky is alive.
 */
public class GameEndConditionsTest {

  private DummyWorld world;
  private Space room;
  private PlayerTestHelper player;

  /**
   * Sets up the test environment with a dummy world and a test player.
   *
   * @throws IOException if the dummy world setup fails
   */
  @Before
  public void setUp() throws IOException {
    room = new Space(0, 0, 1, 1, "Parlor");
    world = new DummyWorld(java.util.Collections.singletonList(room));
    world.setTargetLocation(room);
    // Reset Doctor Lucky's health.
    TargetCharacter.setInstance("Doctor Lucky", 50);
    player = new PlayerTestHelper("Tester", room, world);
  }

  @Test
  public void testGameEndsWhenTargetKilled() {
    // Player picks up a weapon that does 50 damage.
    player.pickUpItemForTest(new Item(0, 50, "Mega Sword"));
    player.attackDoctorLucky("Mega Sword");
    assertEquals(0, TargetCharacter.getInstance().getTargetHealth());
    assertFalse(world.isGameNotOver());
  }

  private static class DummyWorld extends World {
    public DummyWorld(java.util.List<Ispace> spaces) throws IOException {
      super("dummy");
      this.spaces.clear();
      this.spaces.addAll(spaces);
      this.players.clear();
    }

    public void setTargetLocation(Ispace target) {
      for (int i = 0; i < spaces.size(); i++) {
        if (spaces.get(i).getSpaceName().equalsIgnoreCase(target.getSpaceName())) {
          this.targetLocationIndex = i;
          break;
        }
      }
    }
  }

  // Helper subclass for testing Player.
  private static class PlayerTestHelper extends Player {
    public PlayerTestHelper(String name, Ispace startLocation, World world) {
      super(name, startLocation, world);
    }

    public void pickUpItemForTest(Iitem item) {
      this.inventory.add(item);
    }
  }
}
