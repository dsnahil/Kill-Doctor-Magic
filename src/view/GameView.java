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
 * Swing GUI for Kill Doctor Lucky. • Background image • Scrollable, resizable
 * map • Button + key controls • Click on player icon to describe
 */
public class GameView extends JFrame implements IView {
  private final MapPanel mapPanel = new MapPanel();
  private final JScrollPane mapScroll = new JScrollPane(mapPanel);
  private final JTextArea logArea = new JTextArea(6, 40);
  private final JLabel statusLabel = new JLabel();
  private IViewFeatures features;

  private List<Iplayer> players = Collections.emptyList();
  private Ispace target;

  public GameView() throws IOException {
    super("Kill Doctor Lucky");

    // set a spooky background
    try {
      Image bg = ImageIO.read(new File("res/spooky_bg.jpeg"));
      setContentPane(new BackgroundPanel(bg));
    } catch (IOException e) {
      getContentPane().setLayout(new BorderLayout());
    }

    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setMinimumSize(new Dimension(300, 300));

    createMenuBar();
    initUI();
    // initKeyBindings moved to setViewFeatures
    pack();
    setLocationRelativeTo(null);
  }

  private void createMenuBar() {
    JMenuBar mb = new JMenuBar();
    JMenu file = new JMenu("File");
    JMenuItem mNewW = new JMenuItem("New World…");
    JMenuItem mNew = new JMenuItem("Restart Game");
    JMenuItem mQuit = new JMenuItem("Quit");
    mNewW.addActionListener(e -> {
      if (features != null)
        features.handleNewWorld();
    });
    mNew.addActionListener(e -> {
      if (features != null)
        features.handleNewGame();
    });
    mQuit.addActionListener(e -> {
      if (features != null)
        features.handleQuit();
    });
    file.add(mNewW);
    file.add(mNew);
    file.addSeparator();
    file.add(mQuit);

    JMenu help = new JMenu("Help");
    JMenuItem about = new JMenuItem("Instructions");
    about.addActionListener(e -> showInstructions());
    help.add(about);

    mb.add(file);
    mb.add(help);
    setJMenuBar(mb);
  }

  private void initUI() {
    Container c = getContentPane();
    c.setLayout(new BorderLayout(5, 5));

    statusLabel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    c.add(statusLabel, BorderLayout.NORTH);

    mapScroll.setBorder(new TitledBorder("World Map"));
    c.add(mapScroll, BorderLayout.CENTER);

    JPanel east = new JPanel(new GridLayout(0, 1, 5, 5));
    for (String name : new String[] { "Next Turn", "Move", "Pickup", "Look", "Attack", "Describe",
        "Save Map", "Move Pet" }) {
      JButton b = new JButton(name);
      b.setToolTipText(name + " (" + name.charAt(0) + ")");
      b.addActionListener(e -> handleButton(name));
      east.add(b);
    }
    c.add(east, BorderLayout.EAST);

    logArea.setEditable(false);
    JScrollPane logScroll = new JScrollPane(logArea);
    logScroll.setBorder(new TitledBorder("Game Log"));
    c.add(logScroll, BorderLayout.SOUTH);

    mapPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (features != null) {
          features.handleMapClick(e.getX(), e.getY());
        }
      }
    });
  }

  private void initKeyBindings() {
    InputMap im = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
    ActionMap am = getRootPane().getActionMap();

    bind(im, am, 'N', () -> features.handleNextTurn());
    bind(im, am, 'M', () -> features.handleMove());
    bind(im, am, 'P', () -> features.handlePickup());
    bind(im, am, 'L', () -> features.handleLook());
    bind(im, am, 'A', () -> features.handleAttack());
    bind(im, am, 'D', () -> features.handleDescribe());
  }

  private void bind(InputMap im, ActionMap am, char key, Runnable action) {
    String name = "key" + key;
    im.put(KeyStroke.getKeyStroke(key), name);
    am.put(name, new AbstractAction() {
      public void actionPerformed(ActionEvent e) {
        if (features != null) {
          action.run();
        }
      }
    });
  }

  private void handleButton(String name) {
    if (features == null)
      return;
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
  }

  private void showInstructions() {
    String msg = "<html><h2>Kill Doctor Lucky</h2>" + "<ul>"
        + "<li>Keys: N=Next, M=Move, P=Pickup, L=Look, A=Attack, D=Describe</li>"
        + "<li>Click on map to move (you’ll be prompted)</li>"
        + "<li>Click your icon to see your stats</li>" + "</ul></html>";
    JOptionPane.showMessageDialog(this, msg, "Instructions", JOptionPane.INFORMATION_MESSAGE);
  }

  // IView

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
    initKeyBindings();
  }

  /**
   * Controller now passes a **rotated** list so players.get(0) is always the
   * current player.
   */
  public void setEntities(List<Iplayer> players, Ispace target) {
    this.players = players;
    this.target = target;

    String current = players.isEmpty() ? "—" : players.get(0).getPlayerName();
    statusLabel.setText("Players: " + players.size() + "   Current: " + current + "   Target: "
        + (target != null ? target.getSpaceName() : "—"));
  }

  // — draws the map + icons —
  private class MapPanel extends JPanel {
    private BufferedImage img;
    private final int scale = 10;

    void setImage(BufferedImage map) {
      this.img = map;
      if (map != null) {
        setPreferredSize(new Dimension(map.getWidth(), map.getHeight()));
        revalidate();
      }
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      if (img == null)
        return;

      Graphics2D g2 = (Graphics2D) g;
      // smooth everything
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

      // draw the styled base map
      g2.drawImage(img, 0, 0, null);

      // draw Doctor Lucky (red square)
      if (target != null) {
        int cx = ((target.getUpperColumn() + target.getLowerColumn()) / 2) * scale;
        int cy = ((target.getUpperRow() + target.getLowerRow()) / 2) * scale;
        g2.setColor(Color.RED);
        g2.fillRect(cx - 6, cy - 6, 12, 12);
      }

      // draw players (green for current, blue for others)
      for (int i = 0; i < players.size(); i++) {
        Iplayer p = players.get(i);
        Ispace s = p.getPlayerLocation();
        int cx = ((s.getUpperColumn() + s.getLowerColumn()) / 2) * scale;
        int cy = ((s.getUpperRow() + s.getLowerRow()) / 2) * scale;
        g2.setColor(i == 0 ? Color.GREEN : Color.BLUE);
        g2.fillOval(cx - 6, cy - 6, 12, 12);
      }
    }
  }

  // paints a full‑window background
  private static class BackgroundPanel extends JPanel {
    private final Image bg;

    BackgroundPanel(Image bg) {
      this.bg = bg;
      setLayout(new BorderLayout());
    }

    @Override
    protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      g.drawImage(bg, 0, 0, getWidth(), getHeight(), this);
    }
  }
}
