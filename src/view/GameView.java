package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import killdoctorlucky.model.Ipet;
import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Ispace;

/**
 * The main GUI for the Kill Doctor Lucky game. Draws a full-window spooky
 * background image, then layers the map, controls, and log transparently on
 * top.
 */
public class GameView extends JFrame implements Iview {

  private static final long serialVersionUID = 1L;

  // UI components
  private final MapPanel mapPanel = new MapPanel();
  private final JScrollPane mapScroll = new JScrollPane(mapPanel);
  private final JTextArea logArea = new JTextArea(5, 40);
  private final JLabel statusLabel = new JLabel("Welcome!");
  private final JPanel eastButtonsPanel = new JPanel(new GridLayout(0, 1, 5, 5));

  // Controller callback interface
  private IviewFeatures features;

  // Background image
  private BufferedImage background;

  /**
   * Constructs the GameView GUI and initializes all components.
   */
  public GameView() {
    super("Kill Doctor Lucky");

    // Load the background image (try PNG first, fall back to JPEG)
    File backgroundFile = new File("res/spooky_bg.png");
    System.out.println("Looking for background image at: " + backgroundFile.getAbsolutePath());
    if (!backgroundFile.exists()) {
      System.err.println("Background image not found at: " + backgroundFile.getAbsolutePath());
      backgroundFile = new File("res/spooky_bg.jpeg");
      System.out.println("Falling back to JPEG at: " + backgroundFile.getAbsolutePath());
      if (!backgroundFile.exists()) {
        System.err
            .println("JPEG background image not found at: " + backgroundFile.getAbsolutePath());
        background = null;
      } else {
        background = tryLoadImage(backgroundFile);
      }
    } else {
      background = tryLoadImage(backgroundFile);
    }

    // Make a content panel that paints the background
    JPanel content = new JPanel() {
      private static final long serialVersionUID = 1L;

      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null && getWidth() > 0 && getHeight() > 0) {
          // Log the panel's size to ensure it's valid
          System.out.println("Drawing background image at size: " + getWidth() + "x" + getHeight());
          // Draw the image, scaling it to the panel's size
          g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        } else {
          // Fallback to a default color (e.g., dark gray) if image is null or panel size
          // is invalid
          if (background == null) {
            System.out.println("Background image is null, using fallback color.");
          } else {
            System.out.println("Panel size invalid (" + getWidth() + "x" + getHeight()
                + "), using fallback color.");
          }
          g.setColor(Color.DARK_GRAY);
          g.fillRect(0, 0, getWidth(), getHeight());
        }
      }
    };
    content.setLayout(new BorderLayout(5, 5));
    content.setOpaque(true); // Ensure the content panel itself is opaque to paint the background
    // Set a preferred size to ensure the panel has a valid size initially
    content.setPreferredSize(new Dimension(800, 600));
    setContentPane(content);

    createMenuBar();
    initUserInterface(content);
    initKeyBindings();

    setMinimumSize(new Dimension(800, 600));
    pack();
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    // Log to the UI if the background failed to load
    if (background == null) {
      appendToLog("Warning: Background image failed to load. Using default background color.");
    }

    // Force a repaint after the UI is fully initialized
    content.repaint();
  }

  /**
   * Helper method to load and convert an image file.
   *
   * @param backgroundFile the file to load
   * @return the loaded and converted BufferedImage, or null if loading fails
   */
  private BufferedImage tryLoadImage(File backgroundFile) {
    try {
      BufferedImage originalImage = ImageIO.read(backgroundFile);
      if (originalImage != null) {
        System.out.println("Background image loaded successfully.");
        System.out.println("Original image dimensions: " + originalImage.getWidth() + "x"
            + originalImage.getHeight());
        // Convert the image to TYPE_INT_ARGB to ensure compatibility with Swing
        BufferedImage convertedImage = new BufferedImage(originalImage.getWidth(),
            originalImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = convertedImage.createGraphics();
        g2d.drawImage(originalImage, 0, 0, null);
        g2d.dispose();
        System.out.println("Converted image dimensions: " + convertedImage.getWidth() + "x"
            + convertedImage.getHeight());
        return convertedImage;
      } else {
        System.err.println(
            "Failed to load background image: ImageIO.read returned null (unsupported format?).");
        return null;
      }
    } catch (IOException e) {
      System.err.println("Failed to load background image: " + e.getMessage());
      return null;
    }
  }

  private void createMenuBar() {
    final JMenuBar menuBar = new JMenuBar();

    final JMenu fileMenu = new JMenu("File");
    JMenuItem newWorld = new JMenuItem("New… (new world)");
    JMenuItem newSame = new JMenuItem("New… (same world)");
    JMenuItem quit = new JMenuItem("Quit");
    newWorld.addActionListener(e -> features.handleNewWorld());
    newSame.addActionListener(e -> features.handleNewGame());
    quit.addActionListener(e -> features.handleQuit());
    fileMenu.add(newWorld);
    fileMenu.add(newSame);
    fileMenu.addSeparator();
    fileMenu.add(quit);

    final JMenu helpMenu = new JMenu("Help");
    JMenuItem about = new JMenuItem("About");
    about.addActionListener(e -> new AboutDialog(this).setVisible(true));
    helpMenu.add(about);

    menuBar.add(fileMenu);
    menuBar.add(helpMenu);
    setJMenuBar(menuBar);
  }

  private void initUserInterface(JPanel content) {
    // Make the status label non-opaque
    statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    statusLabel.setOpaque(false);
    content.add(statusLabel, BorderLayout.NORTH);

    // Make the map scroll pane and its viewport non-opaque
    mapScroll.setBorder(new TitledBorder("World Map"));
    mapScroll.setOpaque(false);
    mapScroll.getViewport().setOpaque(false);
    mapPanel.setOpaque(false); // Ensure the map panel itself is non-opaque
    content.add(mapScroll, BorderLayout.CENTER);

    // Create buttons and make the east panel non-opaque
    for (String label : new String[] { "Move", "Pickup", "Look", "Attack", "Describe",
        "Save Map" }) {
      JButton btn = new JButton(label);
      btn.addActionListener(e -> {
        switch (label) {
          case "Next Turn":
            features.handleNextTurn();
            break;
          case "Move":
            features.handleMove();
            break;
          case "Pickup":
            features.handlePickup();
            break;
          case "Look":
            features.handleLook();
            break;
          case "Attack":
            features.handleAttack();
            break;
          case "Describe":
            features.handleDescribe();
            break;
          case "Save Map":
            features.handleSaveMap();
            break;
          default:
            break;
        }
      });
      eastButtonsPanel.add(btn);
    }
    eastButtonsPanel.setOpaque(false);
    content.add(eastButtonsPanel, BorderLayout.EAST);

    // Make the log scroll pane and its viewport non-opaque
    JScrollPane logScroll = new JScrollPane(logArea);
    logScroll.setBorder(new TitledBorder("Game Log"));
    logScroll.setOpaque(false);
    logScroll.getViewport().setOpaque(false);
    logArea.setOpaque(false); // Make the text area non-opaque
    logArea.setEditable(false);
    content.add(logScroll, BorderLayout.SOUTH);

    mapPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        features.handleMapClick(e.getX(), e.getY());
      }
    });
  }

  private void initKeyBindings() {
    InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap am = getRootPane().getActionMap();

    im.put(KeyStroke.getKeyStroke('M'), "move");
    am.put("move", new AbstractAction() {
      private static final long serialVersionUID = 1L;

      public void actionPerformed(java.awt.event.ActionEvent e) {
        features.handleMove();
      }
    });

    im.put(KeyStroke.getKeyStroke('P'), "pickup");
    am.put("pickup", new AbstractAction() {
      private static final long serialVersionUID = 1L;

      public void actionPerformed(java.awt.event.ActionEvent e) {
        features.handlePickup();
      }
    });

    im.put(KeyStroke.getKeyStroke('L'), "look");
    am.put("look", new AbstractAction() {
      private static final long serialVersionUID = 1L;

      public void actionPerformed(java.awt.event.ActionEvent e) {
        features.handleLook();
      }
    });

    im.put(KeyStroke.getKeyStroke('A'), "attack");
    am.put("attack", new AbstractAction() {
      private static final long serialVersionUID = 1L;

      public void actionPerformed(java.awt.event.ActionEvent e) {
        features.handleAttack();
      }
    });
  }

  @Override
  public void redrawMap(BufferedImage map) {
    mapPanel.setImage(map);
    mapPanel.repaint();
  }

  @Override
  public void appendToLog(String text) {
    logArea.append(text + "\n");
    logArea.setCaretPosition(logArea.getDocument().getLength());
  }

  @Override
  public void setViewFeatures(IviewFeatures f) {
    this.features = f;
  }

  /**
   * Update the status bar.
   *
   * @param txt the text to display in the status bar
   */
  public void setStatusText(String txt) {
    statusLabel.setText(txt);
  }

  /**
   * Show players and Dr. Lucky on the map.
   *
   * @param players list of players to be displayed
   * @param target  the space where Dr. Lucky is currently located
   */
  public void setEntities(List<Iplayer> players, Ispace target) {
    mapPanel.setEntities(players, target);
  }

  /**
   * Store and draw the pet on the map.
   *
   * @param pet the pet to be displayed
   */
  public void setPet(Ipet pet) {
    mapPanel.setPet(pet);
  }

  /**
   * Inner panel that draws the map image plus all entities.
   */
  private class MapPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private BufferedImage img;
    private List<Iplayer> players = Collections.emptyList();
    private Ispace target;
    private Ipet pet;
    private final int scale = 20;

    void setImage(BufferedImage m) {
      this.img = m;
      if (m != null) {
        setPreferredSize(new Dimension(m.getWidth(), m.getHeight()));
        revalidate();
      }
    }

    void setEntities(List<Iplayer> players, Ispace target) {
      this.players = players;
      this.target = target;
    }

    void setPet(Ipet pet) {
      this.pet = pet;
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (img == null) {
        return;
      }
      g.drawImage(img, 0, 0, null);
      Graphics2D g2 = (Graphics2D) g;

      // draw Dr. Lucky (red square)
      if (target != null) {
        int cx = ((target.getUpperColumn() + target.getLowerColumn()) / 2) * scale;
        int cy = ((target.getUpperRow() + target.getLowerRow()) / 2) * scale;
        g2.setColor(Color.RED);
        g2.fillRect(cx - 6, cy - 6, 12, 12);
      }

      // draw players (blue circles)
      g2.setColor(Color.BLUE);
      for (Iplayer p : players) {
        Ispace s = p.getPlayerLocation();
        int cx = ((s.getUpperColumn() + s.getLowerColumn()) / 2) * scale;
        int cy = ((s.getUpperRow() + s.getLowerRow()) / 2) * scale;
        g2.fillOval(cx - 5, cy - 5, 10, 10);
      }

      // draw pet (green triangle)
      if (pet != null) {
        Ispace ps = pet.getCurrentSpace();
        int cx = ((ps.getUpperColumn() + ps.getLowerColumn()) / 2) * scale;
        int cy = ((ps.getUpperRow() + ps.getLowerRow()) / 2) * scale;
        g2.setColor(Color.GREEN);
        int[] xs = { cx, cx - 6, cx + 6 };
        int[] ys = { cy - 6, cy + 6, cy + 6 };
        g2.fillPolygon(xs, ys, 3);
      }
    }
  }
}