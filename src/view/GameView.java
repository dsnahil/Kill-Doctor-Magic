package view;

import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Ispace;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * The main GUI for the Kill Doctor Lucky game. Draws a full-window spooky
 * background image, then layers the map, controls, and log transparently on
 * top.
 */
public class GameView extends JFrame implements IView {

  // — UI components —
  private final MapPanel mapPanel = new MapPanel();
  private final JScrollPane mapScroll = new JScrollPane(mapPanel);
  private final JTextArea logArea = new JTextArea(5, 40);
  private final JLabel statusLabel = new JLabel("Welcome!");
  private final JPanel eastButtonsPanel = new JPanel(new GridLayout(0, 1, 5, 5));

  // Controller callback interface
  private IViewFeatures features;

  // Background image
  private final BufferedImage background;

  /**
   * Constructs the main game window.
   * 
   * @throws IOException if the background image can’t be loaded
   */
  public GameView() throws IOException {
    super("Kill Doctor Lucky");

    // 1) Load your spooky background
    background = ImageIO.read(new File("res/spooky_bg.jpeg"));

    // 2) Build a single content‐pane that paints that background
    JPanel content = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Stretch the background to fill the window
        g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
      }
    };
    content.setLayout(new BorderLayout(5, 5));
    setContentPane(content);

    // 3) Build the rest of your UI _into_ that content panel
    createMenuBar();
    initUI(content);
    initKeyBindings();

    setMinimumSize(new Dimension(800, 600));
    pack();
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  private void createMenuBar() {
    JMenuBar mb = new JMenuBar();

    JMenu file = new JMenu("File");
    JMenuItem mNewW = new JMenuItem("New… (new world)");
    JMenuItem mNew = new JMenuItem("New… (same world)");
    JMenuItem mQuit = new JMenuItem("Quit");
    mNewW.addActionListener(e -> features.handleNewWorld());
    mNew.addActionListener(e -> features.handleNewGame());
    mQuit.addActionListener(e -> features.handleQuit());
    file.add(mNewW);
    file.add(mNew);
    file.addSeparator();
    file.add(mQuit);

    JMenu help = new JMenu("Help");
    JMenuItem about = new JMenuItem("About");
    about.addActionListener(e -> new AboutDialog(this).setVisible(true));
    help.add(about);

    mb.add(file);
    mb.add(help);
    setJMenuBar(mb);
  }

  private void initUI(JPanel content) {
    // — Status bar at top —
    statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    content.add(statusLabel, BorderLayout.NORTH);

    // — World map in center, with transparent background —
    mapScroll.setBorder(new TitledBorder("World Map"));
    mapScroll.setOpaque(false);
    mapScroll.getViewport().setOpaque(false);
    content.add(mapScroll, BorderLayout.CENTER);

    // — Buttons on right —
    for (String name : new String[] { "Next Turn", "Move", "Pickup", "Look", "Attack", "Describe",
        "Save Map", "Move Pet" }) {
      JButton b = new JButton(name);
      b.addActionListener(e -> {
        switch (name) {
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
          case "Move Pet":
            features.handleMovePet();
            break;
        }
      });
      eastButtonsPanel.add(b);
    }
    eastButtonsPanel.setOpaque(false);
    content.add(eastButtonsPanel, BorderLayout.EAST);

    // — Log area at bottom —
    logArea.setEditable(false);
    JScrollPane logScroll = new JScrollPane(logArea);
    logScroll.setBorder(new TitledBorder("Game Log"));
    logScroll.setOpaque(false);
    logScroll.getViewport().setOpaque(false);
    content.add(logScroll, BorderLayout.SOUTH);

    // — Map‐click handler —
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
      public void actionPerformed(ActionEvent e) {
        features.handleMove();
      }
    });

    im.put(KeyStroke.getKeyStroke('P'), "pickup");
    am.put("pickup", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        features.handlePickup();
      }
    });

    im.put(KeyStroke.getKeyStroke('L'), "look");
    am.put("look", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        features.handleLook();
      }
    });

    im.put(KeyStroke.getKeyStroke('A'), "attack");
    am.put("attack", new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        features.handleAttack();
      }
    });
  }

  // — IView API —
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
  public void setViewFeatures(IViewFeatures f) {
    this.features = f;
  }

  // — Entity update before repainting —
  public void setEntities(List<Iplayer> players, Ispace target) {
    mapPanel.setEntities(players, target);
  }

  // — Inner class: draws the map + icons —
  private class MapPanel extends JPanel {
    private BufferedImage img;
    private List<Iplayer> players = Collections.emptyList();
    private Ispace target;
    private final int scale = 10;

    void setImage(BufferedImage map) {
      this.img = map;
      if (map != null) {
        setPreferredSize(new Dimension(map.getWidth(), map.getHeight()));
        revalidate();
      }
    }

    void setEntities(List<Iplayer> players, Ispace target) {
      this.players = players;
      this.target = target;
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (img != null) {
        g.drawImage(img, 0, 0, null);
        Graphics2D g2 = (Graphics2D) g;
        // draw Doctor Lucky (red square)
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
      }
    }
  }
}
