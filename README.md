# Kill Doctor Lucky

**Project Description:**  
Kill Doctor Lucky is a text-based, turn-based game in which human and computer-controlled players navigate a mansion, collect items, and attempt to kill the elusive target character—Doctor Lucky. The game features a dynamic mansion layout, a graphical world map generator, and a unique twist: Doctor Lucky’s pet, which affects room visibility and can be moved (or even wander automatically via a depth-first search traversal).  

## Features

- **Player Management:**
  - Add human-controlled players (e.g., "Alice", "Tony") at specified starting spaces.
  - Add computer-controlled players (e.g., "CPU") that automatically choose actions based on a fixed random generator for predictable testing.

- **Turn-Based Gameplay:**
  - Alternating turns between players.
  - Each turn, players may execute one of several commands.

- **Movement:**
  - Players can move to adjacent rooms only.
  - Neighbor relationships between rooms are established automatically based on room boundaries.

- **Item Pickup:**
  - Players can pick up items present in their current room (subject to an inventory limit).
  - Used items (weapons) become evidence and are removed from play after an attack.

- **Look Around:**
  - The `look` command provides detailed room information including:
    - The room’s name, coordinate boundaries, and list of items.
    - A list of players currently in the room.
    - Information about neighboring spaces. If a neighbor contains Doctor Lucky’s pet, it is marked “Not visible” so that the pet remains hidden from view.

- **Attack:**
  - Players (human or computer-controlled) can attack Doctor Lucky if they are in the same room.
  - Computer-controlled players choose the highest–damage weapon from their inventory.
  - If a player has no weapon, a default “poke in the eye” attack inflicts 1 point of damage.
  - Attacks are canceled if they are seen by any other player.

- **Display Description:**
  - The `describe` command displays a player’s details (name, current location, and inventory).

- **Graphical Map Generation:**
  - The `savemap` command generates a PNG image of the current world map using AWT.
  - The map visually shows room boundaries and labels.

- **Target Character's Pet:**
  - Doctor Lucky’s pet is added via the mansion specification file.
  - The pet is initially placed in the same room as Doctor Lucky.
  - The pet is explicitly listed in a room’s description.
  - Any neighboring room with a pet present is marked as “Not visible.”
  - The pet can be moved by a player using the `movepet` command.
  - **Extra Credit:** An automatic wandering pet feature has been implemented, where the pet moves along a depth-first traversal (DFS) path through all spaces after each turn.

- **Game End Conditions:**
  - The game ends immediately if a player successfully reduces Doctor Lucky’s health to 0, declaring that player the winner.
  - If the maximum number of turns is reached before Doctor Lucky is killed, the game ends in a draw.

## How to Run the Program

1. **Compile the Project:**
   - Ensure you have Java installed (Java 8 or later).
   - From your project’s root directory, compile using your IDE (e.g., Eclipse) or from the command line:
     ```bash
     javac -d bin src/**/*.java
     ```

2. **Run the Driver:**
   - To run the game via the driver, execute:
     ```bash
     java -cp bin controller.Driver
     ```
   - You may specify command-line arguments:
     - **First argument:** Path to the mansion file (default: `res/mansion.txt`).
     - **Second argument:** Maximum number of turns (default: `10`).

   **Example:**
   ```bash
   java -cp bin controller.Driver res/mansion.txt 10
   ```

3. **Gameplay:**
   - When the game starts, follow the on-screen help menu.
   - Enter commands such as `move`, `pickup`, `look`, `attack`, `describe`, `savemap`, `movepet`, or `quit`.

## Example Runs

The repository includes several text files (placed in the **res/** directory) that document sample runs of the game. Here are brief descriptions:

- **run_visibility.txt:**  
  Demonstrates how the room description marks neighboring spaces as “Not visible” when they contain the pet.

- **run_move_pet.txt:**  
  Shows a human player using the `movepet` command to move the pet from one room to another.

- **run_human_attack.txt:**  
  Illustrates a human player attacking Doctor Lucky using a weapon from their inventory.

- **run_cpu_attack.txt:**  
  Demonstrates that a computer-controlled player automatically selects and uses the highest–damage weapon.

- **run_human_win.txt:**  
  A sample transcript where a human player kills Doctor Lucky and wins the game.

- **run_cpu_win.txt:**  
  A sample transcript where a computer-controlled player kills Doctor Lucky and wins the game.

- **run_target_escapes.txt:**  
  Shows the game ending in a draw when the maximum number of turns is reached before Doctor Lucky is killed.

*You can view the complete transcripts in the respective files (e.g., `res/run_visibility.txt`, etc.).*

## Testing

JUnit 4 tests are located under the **test/killdoctorlucky/model/** directory and are organized by functionality:
- **ControllerTest:** Tests the controller’s behavior (e.g., quitting the game).
- **GameEndConditionsTest:** Checks game-ending conditions (target death, draw).
- **PlayerAttackTest:** Verifies player attack functionality.
- **PlayerTest:** Ensures proper behavior for movement, item pickup, and error handling.
- **TargetCharacterTest:** Tests Doctor Lucky’s health management.
- **TestItemCreation:** Validates item creation and properties.
- **WorldTest:** Confirms world initialization, space management, neighbor relationships, and map generation.
- **Command Tests:** There are dedicated test classes (e.g., MoveCommandTest, LookCommandTest, DisplayPlayerCommandTest, SaveMapCommandTest, WanderingPetTest, PetVisibilityTest, etc.) that test each command’s behavior in isolation.

## Directory Structure

- **src/**  
  - **controller/** – Contains the game controller and command implementations.
  - **killdoctorlucky/model/** – Contains the model classes (World, Player, TargetCharacter, Item, Pet, etc.).
  - **util/** – Contains utility classes (e.g., RandomGenerator).
- **res/**  
  - Contains resource files such as `mansion.txt` (the mansion specification) and any additional assets (e.g., graphics JAR if used for map generation).
  - Contains sample run files (e.g., `run_visibility.txt`, `run_move_pet.txt`, etc.).
- **test/**  
  - Contains JUnit test classes in the package `killdoctorlucky.model`.

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
