package killdoctorlucky.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for game ending conditions.
 */
public class GameEnding {
  private World world;
  private TargetCharacter doctorLucky;

  /**
   * Setup for testing game over conditions.
   */
  @Before
  public void setUp() throws IOException {
    world = new World("res/mansion.txt");
    world.addPlayer("Alice", 0);
    // Reset Doctor Lucky's health for consistency
    TargetCharacter.setInstance("Doctor Lucky", 50);
    doctorLucky = TargetCharacter.getInstance();
  }

  @Test
  public void testGameIsNotOverInitially() {
    assertTrue(world.isGameNotOver());
  }

  @Test
  public void testGameEndsWhenDoctorLuckyDies() {
    doctorLucky.decreaseHealth(50);
    assertFalse(world.isGameNotOver());
  }

  @Test
  public void testWinnerIsLastAttackerIfNoNameProvided() {
    world.setLastAttacker("Bob");
    world.setWinner(null);
    assertEquals("Bob", world.getWinner());
  }

  @Test
  public void testGameNotOverWhenDoctorLuckyStillAlive() {
    doctorLucky.decreaseHealth(20);
    assertTrue(world.isGameNotOver());
  }
}
