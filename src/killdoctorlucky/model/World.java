package killdoctorlucky.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The World class represents the environment of the Kill Doctor Lucky game. It
 * contains the rooms, items, and game progression logic.
 */
public class World implements Iworld {
  private int rows;
  private int cols;
  private ItargetCharacter targetCharacter;
  private List<Ispace> spaces;
  private List<Iitem> items;
  private List<Iplayer> players;
  private int targetLocationIndex;
  private String winnerName = null;
  private String lastAttacker = null;
  private Ipet pet;

  /**
   * Constructs a new World object using the given file path.
   *
   * @param filePath the path to the mansion data file.
   * @throws IOException if the file cannot be read.
   */
  public World(String filePath) throws IOException {
    this.spaces = new ArrayList<>();
    this.items = new ArrayList<>();
    this.players = new ArrayList<>();
    loadWorld(filePath);
  }

  /**
   * Loads the world details from the specified file.
   *
   * @param filePath the path to the world specification file.
   * @throws IOException if reading fails or the format is invalid.
   */
  private void loadWorld(String filePath) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      // Parse world details.
      String line = br.readLine();
      if (line == null || line.trim().isEmpty()) {
        throw new IOException("World details line is missing or empty in " + filePath);
      }
      String[] worldDetails = line.trim().split("\\s+", 3);
      if (worldDetails.length != 3) {
        throw new IOException("Expected 3 tokens for world details, found " + worldDetails.length);
      }
      rows = Integer.parseInt(worldDetails[0]);
      cols = Integer.parseInt(worldDetails[1]);

      // Read target character details.
      line = br.readLine();
      if (line == null || line.trim().isEmpty()) {
        throw new IOException("Target character details missing in " + filePath);
      }
      String[] targetDetails = line.trim().split("\\s+", 2);
      if (targetDetails.length < 2) {
        throw new IOException("Invalid target character format in " + filePath);
      }
      int health = Integer.parseInt(targetDetails[0]);
      String targetName = targetDetails[1];
      TargetCharacter.setInstance(targetName, health);
      this.targetCharacter = TargetCharacter.getInstance();

      // New: Read the pet's name (third line).
      line = br.readLine();
      if (line == null || line.trim().isEmpty()) {
        throw new IOException("Target character pet details missing in " + filePath);
      }
      String petName = line.trim();
      this.pet = new Pet(petName, null);

      // Read number of spaces.
      line = br.readLine();
      if (line == null || line.trim().isEmpty()) {
        throw new IOException("Missing space count in " + filePath);
      }
      int numSpaces = Integer.parseInt(line.trim());

      // Read each space.
      for (int i = 0; i < numSpaces; i++) {
        line = br.readLine();
        if (line == null || line.trim().isEmpty()) {
          throw new IOException("Missing space details for space " + i);
        }
        String[] spaceDetails = line.trim().split("\\s+", 5);
        if (spaceDetails.length < 5) {
          throw new IOException("Invalid space format at line " + (i + 3));
        }
        spaces.add(new Space(Integer.parseInt(spaceDetails[0]), Integer.parseInt(spaceDetails[1]),
            Integer.parseInt(spaceDetails[2]), Integer.parseInt(spaceDetails[3]), spaceDetails[4]));
      }

      // Read number of items.
      line = br.readLine();
      if (line == null || line.trim().isEmpty()) {
        throw new IOException("Missing item count in " + filePath);
      }
      int numItems = Integer.parseInt(line.trim());

      // Read each item.
      for (int i = 0; i < numItems; i++) {
        line = br.readLine();
        if (line == null || line.trim().isEmpty()) {
          throw new IOException("Missing item details for item " + i);
        }
        String[] itemDetails = line.trim().split("\\s+", 3);
        if (itemDetails.length < 3) {
          throw new IOException("Invalid item format at line " + (i + numSpaces + 4));
        }
        items.add(new Item(Integer.parseInt(itemDetails[0]), Integer.parseInt(itemDetails[1]),
            itemDetails[2]));
      }

      // Assign items to spaces.
      for (Iitem item : items) {
        int spaceIndex = item.getSpaceIndex();
        if (spaceIndex >= 0 && spaceIndex < spaces.size()) {
          ((Space) spaces.get(spaceIndex)).addItem(item);
        } else {
          System.out.println(
              "Warning: item " + item.getItemName() + " has an invalid space index: " + spaceIndex);
        }
      }
      establishNeighbors();

      // Initialize target location and place the pet there.
      targetLocationIndex = 0; // Example: target starts at the first space.
      Ispace targetSpace = spaces.get(targetLocationIndex);
      pet.moveTo(targetSpace);
      if (targetSpace instanceof Space) {
        ((Space) targetSpace).setHasPet(true);
      }
    }
  }

  @Override
  public void setLastAttacker(String name) {
    this.lastAttacker = name;
  }

  @Override
  public void addPlayer(String name, int startSpaceIndex) {
    if (startSpaceIndex < 0 || startSpaceIndex >= spaces.size()) {
      throw new IllegalArgumentException(
          "Invalid space index " + startSpaceIndex + " for player start location.");
    }
    players.add(new Player(name, spaces.get(startSpaceIndex), this));
    System.out.println(
        "Player " + name + " added at space " + spaces.get(startSpaceIndex).getSpaceName());
  }

  @Override
  public List<Iplayer> getPlayers() {
    return players;
  }

  @Override
  public BufferedImage generateWorldMap() {
    int scale = 10;
    BufferedImage image = new BufferedImage(cols * scale, rows * scale, BufferedImage.TYPE_INT_RGB);
    Graphics g = image.getGraphics();
    g.setColor(Color.WHITE);
    g.fillRect(0, 0, image.getWidth(), image.getHeight());
    g.setColor(Color.BLACK);
    for (Ispace space : spaces) {
      int x = space.getUpperColumn() * scale;
      int y = space.getUpperRow() * scale;
      int width = (space.getLowerColumn() - space.getUpperColumn()) * scale;
      int height = (space.getLowerRow() - space.getUpperRow()) * scale;
      g.drawRect(x, y, width, height);
      g.drawString(space.getSpaceName(), x + 2, y + 12);
    }
    return image;
  }

  @Override
  public List<String> getPlayerItems() {
    List<String> result = new ArrayList<>();
    for (Iplayer p : players) {
      result.addAll(p.getPlayerItems());
    }
    return result;
  }

  @Override
  public List<String> getSpaceItems() {
    List<String> result = new ArrayList<>();
    for (Ispace s : spaces) {
      for (String item : s.getItems()) {
        result.add(item + " in " + s.getSpaceName());
      }
    }
    return result;
  }

  @Override
  public boolean isGameNotOver() {
    return targetCharacter.getTargetHealth() > 0;
  }

  @Override
  public int findPlayerIndex(String name) {
    for (int i = 0; i < players.size(); i++) {
      if (players.get(i).getPlayerName().equalsIgnoreCase(name)) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public String getSpaceInfo(int index) {
    if (index < 0 || index >= spaces.size()) {
      return "Invalid space index";
    }
    Ispace space = spaces.get(index);
    return space.getSpaceName() + ": (Row " + space.getUpperRow() + ", Col "
        + space.getUpperColumn() + ") - (Row " + space.getLowerRow() + ", Col "
        + space.getLowerColumn() + ")";
  }

  @Override
  public String getSpaceInfo(String spaceName) {
    Ispace s = getSpaceByName(spaceName);
    if (s == null) {
      return "Space not found: " + spaceName;
    }
    StringBuilder sb = new StringBuilder();
    sb.append("Space: ").append(s.getSpaceName()).append("\n").append("Items: ")
        .append(s.getItems()).append("\n").append("Players Present: ");
    List<String> playersInSpace = new ArrayList<>();
    for (Iplayer player : getPlayers()) {
      if (player.getPlayerLocation().getSpaceName().equalsIgnoreCase(s.getSpaceName())) {
        playersInSpace.add(player.getPlayerName());
      }
    }
    sb.append(playersInSpace).append("\n").append("Coordinates: [").append(s.getUpperRow())
        .append(", ").append(s.getUpperColumn()).append("] to [").append(s.getLowerRow())
        .append(", ").append(s.getLowerColumn()).append("]\n")
        .append("--- Visible Neighboring Spaces ---\n");
    for (String neighborName : s.getNeighbors()) {
      Ispace neighbor = getSpaceByName(neighborName);
      if (neighbor != null) {
        if (((Space) neighbor).getHasPet()) {
          sb.append(neighbor.getSpaceName()).append(" (Not visible)\n");
        } else {
          sb.append(neighbor.getSpaceName()).append(" (Items: ").append(neighbor.getItems())
              .append(")\n");
        }
      }
    }
    return sb.toString();
  }

  @Override
  public Ispace getSpaceByName(String name) {
    for (Ispace space : spaces) {
      if (space.getSpaceName().equalsIgnoreCase(name)) {
        return new Space((Space) space);
      }
    }
    System.out.println("Warning: Space '" + name + "' not found.");
    return spaces.isEmpty() ? null : new Space((Space) spaces.get(0));
  }

  @Override
  public Ispace getTargetLocation() {
    return spaces.get(targetLocationIndex);
  }

  @Override
  public void moveTargetCharacter() {
    targetLocationIndex = (targetLocationIndex + 1) % spaces.size();
  }

  /**
   * Establishes neighbors among spaces by checking adjacency.
   */
  private void establishNeighbors() {
    for (int i = 0; i < spaces.size(); i++) {
      Space s1 = (Space) spaces.get(i);
      for (int j = i + 1; j < spaces.size(); j++) {
        Space s2 = (Space) spaces.get(j);
        if (areAdjacent(s1, s2)) {
          s1.addNeighbor(s2.getSpaceName());
          s2.addNeighbor(s1.getSpaceName());
        }
      }
    }
  }

  /**
   * Determines if two spaces are adjacent based on row/column overlap.
   */
  private boolean areAdjacent(Space s1, Space s2) {
    boolean horizontallyAdjacent = (Math.abs(s1.getLowerRow() - s2.getUpperRow()) <= 1
        || Math.abs(s2.getLowerRow() - s1.getUpperRow()) <= 1)
        && overlap(s1.getUpperColumn(), s1.getLowerColumn(), s2.getUpperColumn(),
            s2.getLowerColumn());
    boolean verticallyAdjacent = (Math.abs(s1.getLowerColumn() - s2.getUpperColumn()) <= 1
        || Math.abs(s2.getLowerColumn() - s1.getUpperColumn()) <= 1)
        && overlap(s1.getUpperRow(), s1.getLowerRow(), s2.getUpperRow(), s2.getLowerRow());
    return horizontallyAdjacent || verticallyAdjacent;
  }

  /**
   * Checks if two intervals [start1, end1] and [start2, end2] overlap.
   */
  private boolean overlap(int start1, int end1, int start2, int end2) {
    return !(end1 < start2 || end2 < start1);
  }

  @Override
  public String getWinner() {
    return (winnerName != null) ? winnerName : "No one";
  }

  @Override
  public void setWinner(String name) {
    this.winnerName = (name != null) ? name : lastAttacker;
  }

  @Override
  public String viewTargetCharacter() {
    return targetCharacter.getTargetName() + " (" + targetCharacter.getTargetHealth() + " HP) at "
        + getTargetLocation().getSpaceName();
  }

  @Override
  public Ipet getPet() {
    return pet;
  }

  @Override
  public boolean canPlayerSee(Iplayer a, Iplayer b) {
    Ispace first = a.getPlayerLocation();
    Ispace second = b.getPlayerLocation();
    if (first.getSpaceName().equalsIgnoreCase(second.getSpaceName())) {
      return true;
    }
    return first.getNeighbors().contains(second.getSpaceName());
  }
}
