# Kill Doctor Lucky Game

A text-based game where players navigate a mansion, pick up weapons, and attempt to kill Doctor Lucky.

## How to Play
1. The game starts with a mansion loaded from a file.
2. Players can move between rooms, pick up items, and attack Doctor Lucky.
3. The game ends when Doctor Lucky's health reaches zero.

## Example Runs

- **Example Run 1 (res/example_run1.txt):
  This file demonstrates a complete game session where the player picks up items, moves between spaces, and attacks Doctor Lucky until the game ends. Key functionalities such as movement, item pickup, and attack are showcased.

- Example Run 2 (optional, if you create more):  
  This run could show an edge case or alternate scenario, like failing to pick up an item or attacking without a weapon.

## Commands

- move: Move to an adjacent room.
- pickup: Pick up an item from the current room.
- attack: Attack Doctor Lucky with a weapon in your inventory.
- help: Show the help menu.
- quit: Quit the game.

### Project Setup

1. Clone the repository.
2. Run the `Main.java` file to start the game.
3. Ensure the `mansion.txt` file is in the `res/` directory.

## Test Cases

Test cases are provided in the `test/` directory to ensure the game mechanics work correctly:

- GameEndingTest.java: Tests for game-over conditions.
- PlayerAttackTest.java: Tests for player actions, including attacks on Doctor Lucky.
- WorldTest.java: Tests for the world setup, space validation, and player movement.


## License
MIT License
