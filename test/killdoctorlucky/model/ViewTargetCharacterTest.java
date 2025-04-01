package killdoctorlucky.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests that viewTargetCharacter returns the expected string.
 */
public class ViewTargetCharacterTest {

  private DummyWorld world;
  private Space room;

  /**
   * Sets up the test environment for verifying target character info display.
   *
   * @throws IOException if setup fails
   */
  @Before
  public void setUp() throws IOException {
    room = new Space(0, 0, 1, 1, "TestRoom");
    world = new DummyWorld(java.util.Collections.singletonList(room));
    world.setTargetLocation(room);
    TargetCharacter.setInstance("Doctor Lucky", 50);
  }

  @Test
  public void testViewTargetCharacter() {
    String view = world.viewTargetCharacter();
    assertEquals("Doctor Lucky (50 HP) at TestRoom", view);
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
}
