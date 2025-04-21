package killdoctorlucky.model;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests that when a room contains the target character’s pet, its detailed
 * description explicitly lists the pet.
 */
public class PetVisibilityTest {

  private DummyWorld world;
  private Space roomWithPet;
  private Pet pet;

  @Before
  public void setUp() {
    // create two connected rooms
    roomWithPet = new Space(0, 0, 1, 1, "TestRoom");
    roomWithPet.getNeighbors().add("NeighborRoom");

    Space neighborRoom = new Space(0, 1, 1, 2, "NeighborRoom");
    neighborRoom.getNeighbors().add("TestRoom");

    // inject into our no‑I/O DummyWorld
    List<Ispace> spaces = Arrays.asList(roomWithPet, neighborRoom);
    world = new DummyWorld(spaces);

    // put the pet in roomWithPet
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

  /**
   * A World subclass that never performs any file I/O. We override exactly the
   * package‑private loadWorld(String) hook that the real World(String)
   * constructor calls.
   */
  private static class DummyWorld extends World {
    public DummyWorld(List<Ispace> spaces) {
      super("dummy"); // will dispatch to our override below
      this.spaces.clear();
      this.spaces.addAll(spaces);
      this.players.clear();
    }

    // must match the real signature exactly (no throws, default visibility)
    @Override
    void loadWorld(String unusedFilename) {
      // no-op: skip loading from disk
    }

    /** Test helper to inject the pet. */
    public void setPet(Pet pet) {
      this.pet = pet;
    }
  }
}
