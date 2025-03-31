package killdoctorlucky.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the TargetCharacter class (Doctor Lucky).
 */
public class TargetCharacterTest {
  private TargetCharacter doctorLucky;

  /**
   * Setup for testing Doctor Lucky's attributes and behaviors.
   */
  @Before
  public void setUp() {
    TargetCharacter.setInstance("Doctor Lucky", 50);
    doctorLucky = TargetCharacter.getInstance();
  }

  @Test
  public void testTargetCharacterInitialHealth() {
    assertEquals(50, doctorLucky.getTargetHealth());
  }

  @Test
  public void testDecreaseHealth() {
    doctorLucky.decreaseHealth(10);
    assertEquals(40, doctorLucky.getTargetHealth());
  }

  @Test
  public void testDoctorLuckyCannotHaveNegativeHealth() {
    doctorLucky.decreaseHealth(100);
    assertTrue(doctorLucky.getTargetHealth() <= 0);
  }

  @Test
  public void testGetTargetName() {
    assertEquals("Doctor Lucky", doctorLucky.getTargetName());
  }

  @Test
  public void testDecreaseHealthWithZeroDamage() {
    int initialHealth = doctorLucky.getTargetHealth();
    doctorLucky.decreaseHealth(0);
    assertEquals(initialHealth, doctorLucky.getTargetHealth());
  }

  @Test
  public void testDecreaseHealthExactlyToZero() {
    TargetCharacter.setInstance("Doctor Lucky", 10);
    doctorLucky.decreaseHealth(10);
    assertEquals(0, doctorLucky.getTargetHealth());
  }
}
