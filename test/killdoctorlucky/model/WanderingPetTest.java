package killdoctorlucky.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the extra credit wandering pet functionality. It verifies that when
 * movePetAutomatically() is called repeatedly, the pet follows the DFS–computed
 * path.
 */
public class WanderingPetTest {

  private DummyWorld world;
  private Space room1;
  private Space room2;
  private Space room3;
  private Pet pet;

  /**
   * Sets up a simple world with three connected rooms and a pet in Room1. The pet
   * path is calculated using DFS for automatic movement tests.
   *
   * @throws IOException if dummy world setup fails
   */
  @Before
  public void setUp() throws IOException {
    room1 = new Space(0, 0, 1, 1, "Room1");
    room2 = new Space(0, 1, 1, 2, "Room2");
    room3 = new Space(1, 0, 2, 1, "Room3");
    // Define neighbor links for DFS: room1 adjacent to room2 and room3.
    room1.getNeighbors().add("Room2");
    room1.getNeighbors().add("Room3");
    room2.getNeighbors().add("Room1");
    room3.getNeighbors().add("Room1");
    List<Ispace> spaces = Arrays.asList(room1, room2, room3);
    world = new DummyWorld(spaces);

    pet = new Pet("Mi Meow", room1);
    room1.setHasPet(true);
    world.setPet(pet);
    world.setTargetLocation(room1);
  }

  @Test
  public void testWanderingPetMovement() {
    // Assume DFS order is [Room1, Room2, Room3]
    assertEquals("Room1", pet.getCurrentSpace().getSpaceName());
    world.movePetAutomatically(); // Should move to Room2
    assertEquals("Room2", pet.getCurrentSpace().getSpaceName());
    world.movePetAutomatically(); // Then to Room3
    assertEquals("Room3", pet.getCurrentSpace().getSpaceName());
    world.movePetAutomatically(); // Cycle back to Room1
    assertEquals("Room1", pet.getCurrentSpace().getSpaceName());
  }

  private static class DummyWorld extends World {
    public DummyWorld(List<Ispace> spaces) throws IOException {
      super("dummy");
      this.spaces.clear();
      this.spaces.addAll(spaces);
      this.players.clear();
      // Recompute DFS path for the pet.
      this.petPath = computePetPath();
      this.petIndex = 0;
    }

    public void setPet(Pet pet) {
      this.pet = pet;
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
}
