package killdoctorlucky.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import util.RandomGenerator;

/**
 * Tests that a computer-controlled player chooses the highest–damage weapon
 * when attacking.
 */
public class ComputerPlayerAttackTest {

  private DummyWorld world;
  private Space room;
  private ComputerPlayerTestHelper cpu;

  /**
   * Sets up the game world with one room and a CPU player with multiple items.
   *
   * @throws IOException if something goes wrong with world initialization
   */
  @Before
  public void setUp() throws IOException {
    room = new Space(0, 0, 1, 1, "BattleRoom");
    world = new DummyWorld(Arrays.asList(room));
    world.setTargetLocation(room);
    TargetCharacter.setInstance("Doctor Lucky", 50);
    // Set up a fixed random generator that returns 2 (attack action)
    cpu = new ComputerPlayerTestHelper("CPU", room, world, new RandomGenerator(2));
    world.getPlayers().add(cpu);
  }

  @Test
  public void testDeterministicAttack() {
    // Add two weapons: one doing 5 damage, one doing 10 damage.
    cpu.pickUpItemForTest(new Item(0, 5, "Weak Sword"));
    cpu.pickUpItemForTest(new Item(0, 10, "Strong Sword"));
    cpu.takeTurn();
    // Expect Doctor Lucky's health to drop by 10.
    assertEquals(40, TargetCharacter.getInstance().getTargetHealth());
  }

  private static class DummyWorld extends World {
    public DummyWorld(List<Ispace> spaces) throws IOException {
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

    @Override
    public List<Iplayer> getPlayers() {
      return players;
    }
  }

  // Helper subclass for testing ComputerPlayer.
  private static class ComputerPlayerTestHelper extends ComputerPlayer {
    public ComputerPlayerTestHelper(String name, Ispace startLocation, Iworld world,
        RandomGenerator rg) {
      super(name, startLocation, world, rg);
    }

    public void pickUpItemForTest(Iitem item) {
      this.inventory.add(item);
    }
  }
}
