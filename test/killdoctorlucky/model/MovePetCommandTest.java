package killdoctorlucky.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import controller.commands.MovePetCommand;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests that the MovePetCommand correctly moves the pet from one room to
 * another.
 */
public class MovePetCommandTest {

  private DummyWorld world;
  private Space room1;
  private Space room2;
  private Pet pet;

  /**
   * Initializes a dummy world with two rooms and a pet to test the MovePetCommand
   * functionality.
   *
   * @throws IOException if dummy world setup fails
   */
  @Before
  public void setUp() throws IOException {
    room1 = new Space(0, 0, 1, 1, "Room1");
    room2 = new Space(0, 1, 1, 2, "Room2");
    // Define neighbor relationship.
    room1.getNeighbors().add("Room2");
    room2.getNeighbors().add("Room1");
    List<Ispace> spaces = Arrays.asList(room1, room2);
    world = new DummyWorld(spaces);

    pet = new Pet("Mi Meow", room1);
    room1.setHasPet(true);
    world.setPet(pet);
  }

  @Test
  public void testMovePetCommand() {
    // Move pet from Room1 to Room2.
    MovePetCommand command = new MovePetCommand("Room2");
    command.execute(world);
    assertEquals("Room2", pet.getCurrentSpace().getSpaceName());
    assertTrue(world.getSpaceByName("Room2").getHasPet());
    assertFalse(world.getSpaceByName("Room1").getHasPet());
  }

  private static class DummyWorld extends World {
    public DummyWorld(List<Ispace> spaces) throws IOException {
      super("dummy");
      this.spaces.clear();
      this.spaces.addAll(spaces);
      this.players.clear();
    }

    public void setPet(Pet pet) {
      this.pet = pet;
    }
  }
}
