# Kill Doctor Lucky

**Project Description:**  
Kill Doctor Lucky is a text‑based, turn‑based game in which human and computer‑controlled players navigate a mansion, collect items, and attempt to kill the elusive target character—Doctor Lucky. The game features a dynamic mansion layout, a graphical world map generator, and a unique twist: Doctor Lucky’s pet, which affects room visibility and can be moved (or even wander automatically via a depth‑first search traversal).

---

## Features

- **Strong Separation of Concerns**  
  - Model, controller, and view are fully decoupled to be tested in isolation.
  - Command pattern encapsulates user actions (move, pickup, look, attack, save map, move pet).

- **Player Management:**  
  - Human players enter their names at startup.  
  - Computer players (`ComputerPlayer`) use a pluggable `RandomGenerator` (fixed‑seed option for deterministic tests).

- **Turn-Based Gameplay:**  
  - Alternating turns between players (human or CPU).  
  - Turn count is tracked; game ends on kill or draw at max turns.

- **Movement & Neighbors:**  
  - Rooms automatically link as “neighbors” based on grid overlap.  
  - Players can only move to adjacent rooms.

- **Inventory & Items:**  
  - Rooms contain items; players pick them up (max 5).  
  - Weapons deal damage; removed after use.  
  - Default unarmed attack (“poke in the eye”) deals 1 damage.

- **Look Around & Visibility:**  
  - `look` prints room details: coordinates, items, players present.  
  - Neighbors marked “Not visible” if Doctor Lucky’s pet is present there.

- **Attack Logic:**  
  - Must share a room with Doctor Lucky and be unseen by any other player or pet.  
  - The computer picks the highest‑damage weapon automatically.

- **Pet Mechanics:**  
  - Pet starts with Doctor Lucky.  
  - Affects visibility in neighbor descriptions.  
  - Can be moved manually (`movepet`) or wanders along a DFS path after each turn.

- **Graphical Map & Save Map:**  
  - `savemap` generates a PNG of the mansion layout (AWT).  
  - Rooms are drawn with boundaries and labels.

- **End Conditions:**  
  - First player to reduce Doctor Lucky’s health to 0.  
  - If max turns are reached, the game ends in a draw.

---

## How to Build & Run

1. **Compile**  
   ```bash
   javac -d bin src/**/*.java
   ```
   or use your IDE (Eclipse, IntelliJ).

2. **Run from Class Files**  
   ```bash
   java -cp bin controller.Driver [res/mansion.txt] [maxTurns]
   ```
   - Defaults: `res/mansion.txt` and `10` turns.

3. **Run Executable JAR**  
   An executable JAR `runnable.jar` is provided in `res/`.  
   ```bash
   java -jar res/runnable.jar [res/mansion.txt] [maxTurns]
   ```

---

## Testing

We now have **comprehensive** JUnit‑4 coverage of all non‑GUI code:

 JUnit 4 tests are located under the **test/killdoctorlucky/model/** directory and are organized by functionality:
 - **ControllerTest:** Tests the controller’s behavior (e.g., quitting the game).
 - **GameEndConditionsTest:** Checks game-ending conditions (target death, draw).
 - **PlayerAttackTest:** Verifies player attack functionality.
 - **PlayerTest:** Ensures proper behavior for movement, item pickup, and error handling.
 - **TargetCharacterTest:** Tests Doctor Lucky’s health management.
 - **TestItemCreation:** Validates item creation and properties.
 - **WorldTest:** Confirms world initialization, space management, neighbor relationships, and map generation.
 - **Command Tests:** There are dedicated test classes (e.g., MoveCommandTest, LookCommandTest, DisplayPlayerCommandTest, SaveMapCommandTest, WanderingPetTest, PetVisibilityTest, etc.) that test each command’s behavior in isolation.
- **Utility Tests**  
  - `RandomGeneratorTest`  
  - `TargetCharacterTest`  

- **Model Tests**  
  - `SpaceTest`  
  - `PlayerTest`  
  - `WorldTest`  

- **Controller Tests**  
  - `ControllerImplHandleCommandTest`  
  - `ControllerTest` (quit command)  

- **Command Tests**  
  - `MoveCommandTest`  
  - `LookCommandTest`  
  - `AttackCommandTest`  
  - `MovePetCommandTest`  
  - `PickupCommandTest`  
  - `DisplayPlayerCommandTest`  
  - `GenerateWorldMapTest`  
  - `GameEndConditionsTest`  
  - `ComputerPlayerTest`  
  - `CanPlayerSeeTest`  
  - `PetVisibilityTest`  

To run all tests:

# If you use Maven/Gradle, just invoke `mvn test` or `gradle test`
# Otherwise, from the project root:
java -cp "bin:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar" org.junit.runner.JUnitCore \
  killdoctorlucky.model.SpaceTest \
  killdoctorlucky.model.PlayerTest \
  …<and so on>…

```

---

## Directory Structure

```
.
- **src/**  
   - **controller/** – Contains the game controller and command implementations.
   - **killdoctorlucky/model/** – Contains the model classes (World, Player, TargetCharacter, Item, Pet, etc.).
   - **util/** – Contains utility classes (e.g., RandomGenerator).
   - **controller/commands/** - What commands are used.
 - **res/**  
   - Contains resource files such as `mansion.txt` (the mansion specification) and any additional assets (e.g., graphics JAR if used for map generation).
   - Contains sample run files (e.g., `run_visibility.txt`, `run_move_pet.txt`, etc.).
 - **test/**  
   - Contains JUnit test classes in the package `killdoctorlucky.model`.
└── README.md
```

---

## Design & Testability Notes

- **Decoupling:**  
  - Model ↔ Controller ↔ View are cleanly separated.  
  - Allows mocking out `Iworld`, `Iplayer`, and `Iview` in tests.

- **Improved Test Coverage:**  
  - Key class invariants and error conditions (null checks, invalid moves, empty pickups) are now unit‑tested.  
  - Both happy‑path and failure scenarios are covered.

- **Extensibility:**  
  - New commands or AI strategies can be added without touching core loops.  
  - Pet behavior is pluggable—switch easily between manual and automatic.
 
 ## Assumptions
 
 - The mansion file (`res/mansion.txt`) is well-formed and follows a consistent format.
 - Players are uniquely identified by their names.
 - Movement is only allowed to spaces that are adjacent, as defined by the neighbor relationships.
 - Doctor Lucky’s health is only modified by valid attacks.
 - The pet is initially placed with Doctor Lucky and is hidden from neighboring spaces.
 - Computer-controlled player behavior is based on a random generator that can be fixed for testing purposes.
 
 ## Limitations
 
 - The neighbor detection algorithm uses a simple tolerance check; very complex mansion layouts might require a more robust solution.
 - The graphical map generation uses basic AWT drawing and may not scale for large or highly detailed layouts.
 - The computer-controlled player’s behavior is relatively simple and may not mimic strategic gameplay.
 
 ## Design Changes
 
 - **Computer-Controlled Players:**  
   - Introduced the `ComputerPlayer` class that extends `Player` and implements automated actions.
 - **Turn-Based System:**  
   - Implemented a turn-based mechanism in `ControllerImpl` that alternates turns between players.
 - **Movement Validation:**  
   - Neighbor relationships are established in the `World` class to ensure players move only to adjacent spaces.
 - **Graphical Map Generation:**  
   - Added functionality in `World` to generate and save a PNG map of the mansion.
 - **Target Character’s Pet:**  
   - Added a pet for Doctor Lucky that is included in the room description.
   - Implemented a command (`movepet`) to move the pet, and an extra credit wandering pet feature that uses DFS traversal.
 - **Testability Enhancements:**  
   - Many fields (such as the DFS path and pet index) have been changed to protected to facilitate unit testing.
   - Decoupled file I/O from game logic using dummy worlds and mock implementations for testing.
 
 ## Citations & References
 
 - **Java SE API Documentation:**  
   - [https://docs.oracle.com/en/java/javase/](https://docs.oracle.com/en/java/javase/)
   - [java.awt package](https://docs.oracle.com/javase/8/docs/api/java/awt/package-summary.html)
 - **JUnit 4 API Documentation:**  
   - [https://junit.org/junit4/javadoc/latest/](https://junit.org/junit4/javadoc/latest/)
 - **Design Patterns and Principles:**  
   - Effective Java by Joshua Bloch
   - Clean Code by Robert C. Martin

Happy hunting—and may the best player kill Doctor Lucky first!
