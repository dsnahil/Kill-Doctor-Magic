package util;

import java.util.Random;

/**
 * Provides random number generation with the option to use a fixed sequence for
 * predictable results in testing.
 */
public class RandomGenerator {
  private Random random;
  private int[] fixedNumbers;
  private int index;

  /**
   * Constructs a RandomGenerator that uses Java's built-in Random for generating
   * numbers.
   */
  public RandomGenerator() {
    this.random = new Random();
  }

  /**
   * Constructs a RandomGenerator with a fixed sequence of numbers. This
   * constructor is useful for testing to produce predictable results.
   *
   * @param fixedNumbers the fixed sequence of numbers to use
   * @throws IllegalArgumentException if no fixed numbers are provided
   */
  public RandomGenerator(int... fixedNumbers) {
    if (fixedNumbers == null || fixedNumbers.length == 0) {
      throw new IllegalArgumentException("Must provide at least one fixed number.");
    }
    this.fixedNumbers = fixedNumbers;
    this.index = 0;
  }

  /**
   * Returns a random integer in the range [0, bound). If a fixed sequence was
   * provided, numbers will be returned from that sequence in order.
   *
   * @param bound the upper bound (exclusive)
   * @return a random integer between 0 (inclusive) and bound (exclusive)
   */
  public int nextInt(int bound) {
    if (fixedNumbers != null) {
      int num = fixedNumbers[index % fixedNumbers.length];
      index++;
      return num % bound;
    }
    return random.nextInt(bound);
  }
}
