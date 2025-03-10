# Kill Doctor Lucky

**Project Description:**  
Kill Doctor Lucky is a text-based game where players (both human and computer-controlled) navigate through a mansion, pick up items, and attempt to kill the target character, Doctor Lucky. The game is turn-based, and each player’s turn may involve moving to an adjacent room, picking up items, looking around, attacking (if in the same room as Doctor Lucky), or performing other commands. Additionally, a graphical representation of the game world can be generated and saved as a PNG file.

## Features

- **Player Addition:**  
  - Add a human-controlled player (e.g., "Alice") at a specified starting space.
  - Add a computer-controlled player (e.g., "CPU") at a specified starting space.
  
- **Movement:**  
  - Players can move to adjacent spaces (neighbors). The game validates that moves are only made to adjacent rooms.

- **Item Pickup:**  
  - Players can pick up items found in their current room, subject to an inventory limit.

- **Look Around:**  
  - A player can look around to display detailed information about the current room, including available items, coordinates, neighbors, and which players are present.

- **Attack:**  
  - A player may attack Doctor Lucky using a weapon from their inventory—but only if they are in the same room as Doctor Lucky.

- **Display Description:**  
  - A command to display a specific player’s details (name, current location, and inventory).

- **Save Map:**  
  - Generate a graphical representation of the current world map and save it as a PNG file in the current directory.
  
- **Turn-Based Gameplay:**  
  - The game alternates turns between players in the order they were added.
  
- **Game Ending:**  
  - The game ends when either Doctor Lucky’s health reaches zero or when the maximum number of turns is reached.

### Prerequisite
- JUnit 4 (for running tests).

## How to Run the Program
1. Navigate to the `res/` directory.
2. Run the JAR file using:
   ```
   java -jar your_project.jar
   ```
3. Ensure Java is installed on your system. Use `java -version` to check.

This JAR file is used by the game when generating and saving the world map (triggered by the `savemap` command).  
**Usage:**  
When you run the game, if you enter the command `savemap`, the program uses the functionality from the JAR file to create and save a PNG file (e.g., `worldmap.png`) in the current directory.

### Running the Game
To run the game using the driver, execute:
```bash
java -cp bin controller.Driver
```
You can also specify command-line arguments:
- **First argument:** Path to the mansion file (default: `res/mansion.txt`).
- **Second argument:** Maximum number of turns (default: `10`).

Example:
```bash
java -cp bin controller.Driver res/mansion.txt 10
```

## Example Runs

The project includes multiple example run files that demonstrate all required functionalities. Below are two sample runs:

### run1.txt
This file demonstrates:
- **Adding Players:**  
  - A human-controlled player "Alice" (added at "Armory")  
  - A computer-controlled player "CPU" (added at "Kitchen")
- **Movement:**  
  - Alice moves from "Armory" to "Dining Hall".
- **Item Pickup:**  
  - Alice picks up the "Revolver" from her current space.
- **Look Around:**  
  - Alice uses the `look` command to display detailed room information (items, neighbors, players present).
- **Turn-Based Play:**  
  - The turns alternate between Alice and CPU.
- **Player Description:**  
  - The `describe` command displays Alice’s details.
- **Save Map:**  
  - The `savemap` command creates and saves the world map as `worldmap.png`.
- **Game End:**  
  - The game ends after the maximum number of turns, resulting in a draw.

*Please see run1.txt for the complete transcript.*

### run2.txt
This file demonstrates:
- **Adding Players:**  
  - Human player "Alice" and computer-controlled player "CPU".
- **Movement:**  
  - Alice moves from "Armory" to "Dining Hall" then to "Armory" again.
- **Item Pickup:**  
  - Both players pick up items from their respective rooms.
- **Look Around & Display Information:**  
  - The `look` command shows room details, and `describe` shows a player’s description.
- **Attack:**  
  - CPU attacks Doctor Lucky when in the same room, decreasing Doctor Lucky’s HP.
- **Save Map:**  
  - A world map is generated and saved.
- **Game End:**  
  - The game ends after the maximum number of turns.

*Please see run2.txt for the complete transcript.*

## Testing

JUnit 4 tests are located in the `test/killdoctorlucky/model/` directory. The tests are organized by functionality:
- **ControllerTest:** Verifies controller commands (e.g., quitting the game).
- **GameEnding:** Checks game-ending conditions.
- **PlayerAttackTest:** Tests player attack functionality.
- **PlayerTest:** Tests player operations such as moving, picking up items, and error handling.
- **TargetCharacterTest:** Tests target character (Doctor Lucky) behaviors.
- **TestItemCreation:** Tests item creation and properties.
- **WorldTest:** Tests world initialization and space-related functionalities.
- **[Command]Test Classes:** There are separate test classes for individual commands (e.g., MoveCommandTest, LookCommandTest, DisplayPlayerCommandTest, SaveMapCommandTest) that test these commands in isolation using minimal mock implementations.

## Directory Structure

- **src/**  
  - **controller/** – Contains controller classes and command implementations.
  - **killdoctorlucky/model/** – Contains model classes (World, Player, TargetCharacter, Item, etc.).
  - **util/** – Contains utility classes (e.g., RandomGenerator).
- **res/**  
  - Contains resource files:
    - `mansion.txt` – The mansion specification.
    - `graphics.jar` – The JAR file used for generating the world map.
- **test/**  
  - Contains JUnit test classes under `killdoctorlucky/model/`.