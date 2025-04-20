package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
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
  private final BufferedImage background;

  /**
   * Constructs the main game window.
   *
   * @throws IOException if the background image can’t be loaded.
   */
  public GameView() throws IOException {
    super("Kill Doctor Lucky");
    background = ImageIO.read(new File("res/spooky_bg.jpeg"));
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

  /**
   * Builds and installs the application menu bar.
   */
  private void createMenuBar() {
    final JMenuBar menuBar = new JMenuBar();

    final JMenu fileMenu = new JMenu("File");
    final JMenuItem newWorldMenuItem = new JMenuItem("New… (new world)");
    final JMenuItem newSameWorldMenuItem = new JMenuItem("New… (same world)");
    final JMenuItem quitMenuItem = new JMenuItem("Quit");
    newWorldMenuItem.addActionListener(e -> features.handleNewWorld());
    newSameWorldMenuItem.addActionListener(e -> features.handleNewGame());
    quitMenuItem.addActionListener(e -> features.handleQuit());
    fileMenu.add(newWorldMenuItem);
    fileMenu.add(newSameWorldMenuItem);
    fileMenu.addSeparator();
    fileMenu.add(quitMenuItem);

    final JMenu helpMenu = new JMenu("Help");
    final JMenuItem aboutMenuItem = new JMenuItem("About");
    aboutMenuItem.addActionListener(e -> new AboutDialog(this).setVisible(true));
    helpMenu.add(aboutMenuItem);

    menuBar.add(fileMenu);
    menuBar.add(helpMenu);
    setJMenuBar(menuBar);
  }

  /**
   * Lays out the main user interface (status bar, map, controls, and log).
   *
   * @param content the main content panel.
   */
  private void initUserInterface(final JPanel content) {
    statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    content.add(statusLabel, BorderLayout.NORTH);

    mapScroll.setBorder(new TitledBorder("World Map"));
    mapScroll.setOpaque(false);
    mapScroll.getViewport().setOpaque(false);
    content.add(mapScroll, BorderLayout.CENTER);

    for (String label : new String[] { "Next Turn", "Move", "Pickup", "Look", "Attack", "Describe",
        "Save Map" }) {
      JButton button = new JButton(label);
      button.addActionListener(e -> {
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
      eastButtonsPanel.add(button);
    }
    eastButtonsPanel.setOpaque(false);
    content.add(eastButtonsPanel, BorderLayout.EAST);

    logArea.setEditable(false);
    JScrollPane logScroll = new JScrollPane(logArea);
    logScroll.setBorder(new TitledBorder("Game Log"));
    logScroll.setOpaque(false);
    logScroll.getViewport().setOpaque(false);
    content.add(logScroll, BorderLayout.SOUTH);

    mapPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        features.handleMapClick(e.getX(), e.getY());
      }
    });
  }

  /**
   * /** Sets up global key bindings for quick actions.
   */
  private void initKeyBindings() {
    InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap actionMap = getRootPane().getActionMap();

    inputMap.put(KeyStroke.getKeyStroke('M'), "move");
    actionMap.put("move", new AbstractAction() {
      private static final long serialVersionUID = 1L;

      @Override
      public void actionPerformed(ActionEvent e) {
        features.handleMove();
      }
    });

    inputMap.put(KeyStroke.getKeyStroke('P'), "pickup");
    actionMap.put("pickup", new AbstractAction() {
      private static final long serialVersionUID = 1L;

      @Override
      public void actionPerformed(ActionEvent e) {
        features.handlePickup();
      }
    });

    inputMap.put(KeyStroke.getKeyStroke('L'), "look");
    actionMap.put("look", new AbstractAction() {
      private static final long serialVersionUID = 1L;

      @Override
      public void actionPerformed(ActionEvent e) {
        features.handleLook();
      }
    });

    inputMap.put(KeyStroke.getKeyStroke('A'), "attack");
    actionMap.put("attack", new AbstractAction() {
      private static final long serialVersionUID = 1L;

      @Override
      public void actionPerformed(ActionEvent e) {
        features.handleAttack();
      }
    });
  }

  @Override
  public void redrawMap(final BufferedImage map) {
    mapPanel.setImage(map);
    mapPanel.repaint();
  }

  @Override
  public void appendToLog(final String text) {
    logArea.append(text + "\n");
    logArea.setCaretPosition(logArea.getDocument().getLength());
  }

  @Override
  public void setViewFeatures(final IviewFeatures f) {
    this.features = f;
  }

  /**
   * Updates the status bar text.
   *
   * @param text the new status text.
   */
  public void setStatusText(final String text) {
    statusLabel.setText(text);
  }

  /**
   * Updates the positions of players and Doctor Lucky on the map.
   *
   * @param players the list of players.
   * @param target  the space containing Doctor Lucky.
   */
  public void setEntities(final List<Iplayer> players, final Ispace target) {
    mapPanel.setEntities(players, target);
  }

  /**
   * Inner panel that draws the map and entity icons.
   */
  private class MapPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private BufferedImage img;
    private List<Iplayer> players = Collections.emptyList();
    private Ispace target;
    private final int scale = 10;

    void setImage(final BufferedImage map) {
      this.img = map;
      if (map != null) {
        setPreferredSize(new Dimension(map.getWidth(), map.getHeight()));
        revalidate();
      }
    }

    void setEntities(final List<Iplayer> players, final Ispace target) {
      this.players = players;
      this.target = target;
    }

    @Override
    protected void paintComponent(final Graphics g) {
      super.paintComponent(g);
      if (img != null) {
        g.drawImage(img, 0, 0, null);
        Graphics2D g2 = (Graphics2D) g;

        if (target != null) {
          int cx = ((target.getUpperColumn() + target.getLowerColumn()) / 2) * scale;
          int cy = ((target.getUpperRow() + target.getLowerRow()) / 2) * scale;
          g2.setColor(Color.RED);
          g2.fillRect(cx - 6, cy - 6, 12, 12);
        }

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
