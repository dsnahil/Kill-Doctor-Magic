package killdoctorlucky.model;

import static org.junit.Assert.assertTrue;

import controller.ControllerImpl;
import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for the ControllerImpl class. This test ensures that the game exits
 * properly when the user inputs "quit".
 */
public class ControllerTest {
  @Test
  public void testControllerQuitCommand() throws IOException {
    String input = "quit\n";
    StringBuilder output = new StringBuilder();
    Iworld world = new World("res/mansion.txt");
    world.addPlayer("Alice", 0);
    ControllerImpl controller = new ControllerImpl(world, new StringReader(input), output, 10);
    controller.startGame();
    // Check that the output includes an exit message.
    assertTrue(output.toString().contains("Exiting the game"));
  }
}
