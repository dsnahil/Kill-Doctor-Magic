
package killdoctorlucky.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Tests for the Item class.
 */
public class TestItemCreation {
  @Test
  public void itemCreation() {
    Item knife = new Item(5, 3, "Sharp Knife");
    assertEquals("Sharp Knife", knife.getItemName());
    assertEquals(3, knife.getDamage());
    assertEquals(5, knife.getSpaceIndex());
  }

  @Test
  public void testItemDamage() {
    Item revolver = new Item(2, 10, "Revolver");
    assertEquals(10, revolver.getDamage());
  }

  @Test
  public void testToStringMethod() {
    Item item = new Item(3, 7, "Chain Saw");
    String expected = "Chain Saw (Damage: 7)";
    assertEquals(expected, item.toString());
  }
}
