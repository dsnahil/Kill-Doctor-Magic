package killdoctorlucky.model;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

/**
 * Contains the entire implementation of the game.
 */
public class World implements Iworld {
  protected int rows;
  protected int cols;
  protected ItargetCharacter targetCharacter;
  protected List<Ispace> spaces;
  protected List<Iplayer> players;
  protected int targetLocationIndex;
  protected Ipet pet;
  protected List<Ispace> petPath; // DFS path for wandering pet
  protected int petIndex;

  // Private fields
  private List<Iitem> items;
  private String winnerName = null;
  private String lastAttacker = null;

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

  private void loadWorld(String filePath) throws IOException {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      // Parse world details.
      String line = br.readLine();
      if (line == null || line.trim().isEmpty()) {
        throw new IOException("World details line is missing or empty in " + filePath);
      }
      String[] worldDetails = line.trim().split("\\s+");
      if (worldDetails.length < 3) {
        throw new IOException(
            "Expected at least 3 tokens for world details, found " + worldDetails.length);
      }
      rows = Integer.parseInt(worldDetails[0]);
      cols = Integer.parseInt(worldDetails[1]);
      // The third token is the mansion name (unused)

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

      // Read the pet's name (third line).
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
        Scanner lineScanner = new Scanner(line);
        try {
          int x1 = lineScanner.nextInt();
          int y1 = lineScanner.nextInt();
          int x2 = lineScanner.nextInt();
          int y2 = lineScanner.nextInt();
          String spaceName = "";
          while (lineScanner.hasNext()) {
            spaceName += lineScanner.next() + " ";
          }
          spaceName = spaceName.trim();
          spaces.add(new Space(x1, y1, x2, y2, spaceName));
        } catch (NumberFormatException | NoSuchElementException e) {
          throw new IOException("Error parsing space details at line " + (i + 4), e);
        } finally {
          lineScanner.close();
        }
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
        Scanner lineScanner = new Scanner(line);
        try {
          int spaceIndex = lineScanner.nextInt();
          int damage = lineScanner.nextInt();
          String itemName = "";
          while (lineScanner.hasNext()) {
            itemName += lineScanner.next() + " ";
          }
          itemName = itemName.trim();
          items.add(new Item(spaceIndex, damage, itemName));
        } catch (NumberFormatException | NoSuchElementException e) {
          throw new IOException("Error parsing item details at line " + (i + numSpaces + 4), e);
        } finally {
          lineScanner.close();
        }
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

      // Compute the DFS path for the wandering pet.
      petPath = computePetPath();
      petIndex = 0;

      // Initialize target location and place the pet there.
      targetLocationIndex = 0; // Target starts at the first space.
      Ispace targetSpace = spaces.get(targetLocationIndex);
      pet.moveTo(targetSpace);
      if (targetSpace instanceof Space) {
        ((Space) targetSpace).setHasPet(true);
      }
    }
  }

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

  private boolean overlap(int start1, int end1, int start2, int end2) {
    return !(end1 < start2 || end2 < start1);
  }

  @Override
  public List<Iplayer> getPlayers() {
    return players;
  }

  @Override
  public void addPlayer(String name, int spaceIndex) {
    if (spaceIndex < 0 || spaceIndex >= spaces.size()) {
      throw new IllegalArgumentException(
          "Invalid space index " + spaceIndex + " for player start location.");
    }
    players.add(new Player(name, spaces.get(spaceIndex), this));
    System.out
        .println("Player " + name + " added at space " + spaces.get(spaceIndex).getSpaceName());
  }

  /**
   * Generates a visual representation of the game world as a BufferedImage. Each
   * room is drawn as a light‐gray filled rectangle with a black border and its
   * name centered inside. A 50px right margin ensures no labels ever get clipped.
   */
  @Override
  public BufferedImage generateWorldMap() {
    final int scale = 10;
    final int margin = 50; // extra room on the right for long labels
    final int imgW = cols * scale + margin;
    final int imgH = rows * scale;

    BufferedImage image = new BufferedImage(imgW, imgH, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2 = image.createGraphics();

    // 1) White background
    g2.setColor(Color.WHITE);
    g2.fillRect(0, 0, imgW, imgH);

    // 2) Draw each room
    for (Ispace space : spaces) {
      int x = space.getUpperColumn() * scale;
      int y = space.getUpperRow() * scale;
      int w = (space.getLowerColumn() - space.getUpperColumn()) * scale;
      int h = (space.getLowerRow() - space.getUpperRow()) * scale;

      // a) fill interior
      g2.setColor(new Color(230, 230, 230));
      g2.fillRect(x, y, w, h);

      // b) outline
      g2.setColor(Color.BLACK);
      g2.drawRect(x, y, w, h);

      // c) room name, centered
      FontMetrics fm = g2.getFontMetrics();
      String name = space.getSpaceName();
      int textW = fm.stringWidth(name);
      int textH = fm.getHeight();
      int tx = x + (w - textW) / 2;
      int ty = y + (h - textH) / 2 + fm.getAscent();
      g2.drawString(name, tx, ty);
    }

    g2.dispose();
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

  /**
   * Information about the space.
   */
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
    sb.append(playersInSpace).append("\n");
    // If the current space contains the pet, list it explicitly.
    if (s.getHasPet() && pet != null) {
      sb.append("Also present: ").append(pet.getName()).append("\n");
    }
    sb.append("Coordinates: [").append(s.getUpperRow()).append(", ").append(s.getUpperColumn())
        .append("] to [").append(s.getLowerRow()).append(", ").append(s.getLowerColumn())
        .append("]\n").append("--- Visible Neighboring Spaces ---\n");
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

  /**
   * Corrected getSpaceByName: now returns the actual instance so that updates
   * (e.g., pet flags) are visible.
   */
  @Override
  public Ispace getSpaceByName(String name) {
    for (Ispace space : spaces) {
      if (space.getSpaceName().equalsIgnoreCase(name)) {
        return space;
      }
    }
    System.out.println("Warning: Space '" + name + "' not found.");
    return spaces.isEmpty() ? null : spaces.get(0);
  }

  @Override
  public String viewTargetCharacter() {
    return targetCharacter.getTargetName() + " (" + targetCharacter.getTargetHealth() + " HP) at "
        + getTargetLocation().getSpaceName();
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
   * Computes a depth-first search (DFS) traversal path for the pet through all
   * spaces.
   *
   * @return a list of spaces representing the DFS traversal order.
   */
  protected List<Ispace> computePetPath() {
    List<Ispace> path = new ArrayList<>();
    Set<String> visited = new HashSet<>();
    if (!spaces.isEmpty()) {
      dfs(spaces.get(0), visited, path);
    }
    return path;
  }

  /**
   * Helper method for DFS traversal.
   *
   * @param space   the current space.
   * @param visited the set of visited space names.
   * @param path    the DFS path list being built.
   */
  private void dfs(Ispace space, Set<String> visited, List<Ispace> path) {
    if (visited.contains(space.getSpaceName())) {
      return;
    }
    visited.add(space.getSpaceName());
    path.add(space);
    for (String neighborName : space.getNeighbors()) {
      Ispace neighbor = getSpaceByName(neighborName);
      if (neighbor != null && !visited.contains(neighbor.getSpaceName())) {
        dfs(neighbor, visited, path);
      }
    }
  }

  /**
   * Moves the pet automatically along the precomputed DFS path. This implements
   * the extra credit wandering pet.
   */
  public void movePetAutomatically() {
    if (petPath == null || petPath.isEmpty() || pet == null) {
      return;
    }
    // Remove pet flag from current space.
    Ispace current = pet.getCurrentSpace();
    if (current != null && current instanceof Space) {
      ((Space) current).setHasPet(false);
    }
    petIndex = (petIndex + 1) % petPath.size();
    Ispace nextSpace = petPath.get(petIndex);
    pet.moveTo(nextSpace);
    if (nextSpace instanceof Space) {
      ((Space) nextSpace).setHasPet(true);
    }
  }

  @Override
  public void setLastAttacker(String name) {
    this.lastAttacker = name;
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
