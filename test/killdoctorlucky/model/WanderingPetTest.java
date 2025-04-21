package killdoctorlucky.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
   */
  @Before
  public void setUp() throws IOException {
    // 1) Create a tiny "dummy" world file on the fly so World.loadWorld(...)
    // succeeds
    Path dummyFile = Files.createTempFile("dummy", ".txt");
    Files.write(dummyFile, Arrays.asList(
        // world grid + (unused) name
        "3 3 TestMansion",
        // target health + name
        "10 Doctor Lucky",
        // pet name
        "Mi Meow",
        // number of spaces
        "3",
        // each space: x1 y1 x2 y2 name
        "0 0 1 1 Room1", "0 1 1 2 Room2", "1 0 2 1 Room3",
        // number of items
        "0"
    // (no item lines follow)
    ));

    // 2) Manually build the three Space instances and their neighbor relationships
    room1 = new Space(0, 0, 1, 1, "Room1");
    room2 = new Space(0, 1, 1, 2, "Room2");
    room3 = new Space(1, 0, 2, 1, "Room3");
    room1.getNeighbors().add("Room2");
    room1.getNeighbors().add("Room3");
    room2.getNeighbors().add("Room1");
    room3.getNeighbors().add("Room1");
    List<Ispace> spaces = Arrays.asList(room1, room2, room3);

    // 3) Instantiate our DummyWorld using the file we just wrote
    world = new DummyWorld(dummyFile.toString(), spaces);

    // 4) Place the pet, mark it in Room1, and fix the target location there too
    pet = new Pet("Mi Meow", room1);
    room1.setHasPet(true);
    world.setPet(pet);
    world.setTargetLocation(room1);
  }

  @Test
  public void testWanderingPetMovement() {
    // The DFS order for these three connected rooms is Room1 → Room2 → Room3 → back
    // to Room1
    assertEquals("Room1", pet.getCurrentSpace().getSpaceName());
    world.movePetAutomatically();
    assertEquals("Room2", pet.getCurrentSpace().getSpaceName());
    world.movePetAutomatically();
    assertEquals("Room3", pet.getCurrentSpace().getSpaceName());
    world.movePetAutomatically();
    assertEquals("Room1", pet.getCurrentSpace().getSpaceName());
  }

  /**
   * A test‐only subclass of World that drives its superclass through the normal
   * loadWorld(path) constructor, then replaces spaces with our handcrafted list.
   */
  private static class DummyWorld extends World {
    public DummyWorld(String filePath, List<Ispace> spaces) throws IOException {
      super(filePath); // loadWorld(...) reads our tiny dummy file
      this.spaces.clear(); // discard whatever got loaded
      this.spaces.addAll(spaces); // install our manual rooms
      this.players.clear(); // no players needed here
      // rebuild the petPath for wandering‐pet logic
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
