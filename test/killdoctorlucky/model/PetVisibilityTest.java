package killdoctorlucky.model;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests that when a room contains the target character's pet, its detailed
 * description explicitly lists the pet.
 */
public class PetVisibilityTest {

  private DummyWorld world;
  private Space roomWithPet;
  private Pet pet;

  /**
   * Initializes a dummy world with two rooms and places a pet in one of them to
   * verify visibility in room descriptions.
   *
   * @throws IOException if dummy world setup fails
   */
  @Before
  public void setUp() throws IOException {
    // ensure there's a file named "dummy" so super("dummy") won't blow up
    File dummy = new File("dummy");
    if (!dummy.exists()) {
      dummy.createNewFile();
    }

    roomWithPet = new Space(0, 0, 1, 1, "TestRoom");
    roomWithPet.getNeighbors().add("NeighborRoom");
    Space neighborRoom = new Space(0, 1, 1, 2, "NeighborRoom");
    neighborRoom.getNeighbors().add("TestRoom");
    List<Ispace> spaces = Arrays.asList(roomWithPet, neighborRoom);

    world = new DummyWorld(spaces);

    // Create a pet and place it in roomWithPet.
    pet = new Pet("Mi Meow", roomWithPet);
    roomWithPet.setHasPet(true);
    world.setPet(pet);
  }

  @Test
  public void testRoomInfoIncludesPet() {
    String info = world.getSpaceInfo("TestRoom");
    assertTrue("The room info should include the pet when present",
        info.contains("Also present: Mi Meow"));
  }

  // DummyWorld subclass for testing (bypasses file reading)
  private static class DummyWorld extends World {
    public DummyWorld(List<Ispace> spaces) throws IOException {
      super("dummy"); // now finds the empty "dummy" file we created above
      this.spaces.clear();
      this.spaces.addAll(spaces);
      this.players.clear();
    }

    public void setPet(Pet pet) {
      this.pet = pet;
    }
  }
}
