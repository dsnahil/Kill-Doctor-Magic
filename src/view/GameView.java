// File: src/view/GameView.java
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
import killdoctorlucky.model.Iplayer;
import killdoctorlucky.model.Ipet;
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

  // The pet
  private Ipet pet;

  // Background image
  private final BufferedImage background;

  public GameView() throws IOException {
    super("Kill Doctor Lucky");
    background = ImageIO.read(new File("res/spooky_bg.jpeg"));

    // make a content panel that paints the background
    JPanel content = new JPanel() {
      private static final long serialVersionUID = 1L;

      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), null);
      }
    };
    content.setLayout(new BorderLayout(5, 5));
    setContentPane(content);

    createMenuBar();
    initUserInterface(content);
    initKeyBindings();

    setMinimumSize(new Dimension(800, 600));
    pack();
    setLocationRelativeTo(null);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
  }

  private void createMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    JMenu fileMenu = new JMenu("File");
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

    JMenu helpMenu = new JMenu("Help");
    JMenuItem about = new JMenuItem("About");
    about.addActionListener(e -> new AboutDialog(this).setVisible(true));
    helpMenu.add(about);

    menuBar.add(fileMenu);
    menuBar.add(helpMenu);
    setJMenuBar(menuBar);
  }

  private void initUserInterface(JPanel content) {
    statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    content.add(statusLabel, BorderLayout.NORTH);

    mapScroll.setBorder(new TitledBorder("World Map"));
    mapScroll.setOpaque(false);
    mapScroll.getViewport().setOpaque(false);
    content.add(mapScroll, BorderLayout.CENTER);

    for (String label : new String[] { "Next Turn", "Move", "Pickup", "Look", "Attack", "Describe",
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
        }
      });
      eastButtonsPanel.add(btn);
    }
    eastButtonsPanel.setOpaque(false);
    content.add(eastButtonsPanel, BorderLayout.EAST);

    JScrollPane logScroll = new JScrollPane(logArea);
    logScroll.setBorder(new TitledBorder("Game Log"));
    logScroll.setOpaque(false);
    logScroll.getViewport().setOpaque(false);
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

  /** Update the status bar. */
  public void setStatusText(String txt) {
    statusLabel.setText(txt);
  }

  /** Show players + Dr. Lucky. */
  public void setEntities(List<Iplayer> players, Ispace target) {
    mapPanel.setEntities(players, target);
  }

  /** NEW: store the pet so we can paint it. */
  public void setPet(Ipet pet) {
    this.pet = pet;
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

      // draw pet (green triangle) — use getCurrentSpace()
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
