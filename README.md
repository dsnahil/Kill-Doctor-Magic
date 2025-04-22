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
├── src/
│   ├── controller/          # Controllers & commands
│   ├── killdoctorlucky/
│   │   └── model/           # Domain model (World, Player, Pet, etc.)
│   └── util/                # Utilities (RandomGenerator)
├── res/                     # Mansion file, sample run transcripts, runnable.jar
├── test/                    # JUnit tests grouped by package
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

---

Happy hunting—and may the best player kill Doctor Lucky first!
