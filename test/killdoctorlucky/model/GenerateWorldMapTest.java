package killdoctorlucky.model;

import static org.junit.Assert.assertNotNull;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests that generateWorldMap returns a non-null image.
 */
public class GenerateWorldMapTest {

  private DummyWorld world;
  private Space room1;
  private Space room2;

  /**
   * Sets up a simple world with two rooms for map generation testing.
   *
   * @throws IOException if world setup fails
   */
  @Before
  public void setUp() throws IOException {
    room1 = new Space(0, 0, 1, 1, "Room1");
    room2 = new Space(0, 1, 1, 2, "Room2");
    room1.getNeighbors().add("Room2");
    room2.getNeighbors().add("Room1");
    world = new DummyWorld(Arrays.asList(room1, room2));
  }

  @Test
  public void testGenerateWorldMapNotNull() {
    BufferedImage map = world.generateWorldMap();
    assertNotNull("The world map image should not be null", map);
  }

  private static class DummyWorld extends World {
    public DummyWorld(java.util.List<Ispace> spaces) throws IOException {
      super("dummy");
      this.spaces.clear();
      this.spaces.addAll(spaces);
      this.players.clear();
    }
  }
}
