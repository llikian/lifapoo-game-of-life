package view_controller;

import model.Environment;
import model.Scheduler;

import java.awt.*;

import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

public class Window extends JFrame implements Observer {
    private final Environment environment;
    private final Scheduler scheduler;
    private final HexaPanel centralPanel;
    private final JLabel infoLabel;

    public Color backgroundColor;
    public Color foregroundColor;
    public Color backgroundBrighter;
    public Color selectionBackground;

    public Window(Environment environment, Scheduler scheduler) {
        super();

        this.environment = environment;
        this.scheduler = scheduler;
        this.centralPanel = new HexaPanel(environment);
        this.infoLabel = new JLabel("");

        this.backgroundColor = new Color(30, 30, 30);
        this.foregroundColor = Color.white;
        this.backgroundBrighter = new Color(50, 50, 50);
        this.selectionBackground = new Color(163, 29, 29);

        /* Background Colors */
        UIManager.put("Panel.background", backgroundColor);
        UIManager.put("Label.background", backgroundBrighter);
        UIManager.put("Button.background", backgroundBrighter);
        UIManager.put("CheckBox.background", backgroundBrighter);
        UIManager.put("MenuBar.background", backgroundBrighter);
        UIManager.put("Menu.background", backgroundBrighter);
        UIManager.put("MenuItem.background", backgroundBrighter);
        UIManager.put("PopupMenu.background", backgroundColor);
        centralPanel.setBackground(backgroundColor);

        /* Foreground Colors */
        UIManager.put("Panel.foreground", foregroundColor);
        UIManager.put("Label.foreground", foregroundColor);
        UIManager.put("Button.foreground", foregroundColor);
        UIManager.put("CheckBox.foreground", foregroundColor);
        UIManager.put("MenuBar.foreground", foregroundColor);
        UIManager.put("Menu.foreground", foregroundColor);
        UIManager.put("MenuItem.foreground", foregroundColor);
        infoLabel.setForeground(foregroundColor);

        /* Selected / Hovered Background Colors */
        UIManager.put("Menu.selectionBackground", selectionBackground);
        UIManager.put("MenuItem.selectionBackground", selectionBackground);
        UIManager.put("Button.selectionBackground", selectionBackground);

        /* Selected / Hovered Foreground Colors */
        UIManager.put("Menu.selectionForeground", foregroundColor);
        UIManager.put("MenuItem.selectionForeground", foregroundColor);
        UIManager.put("Button.selectionForeground", foregroundColor);

        /* Border Thickness */
        UIManager.put("Panel.borderColor", Color.white);
        UIManager.put("MenuBar.borderColor", Color.white);

        /* Border Color */
        UIManager.put("Panel.border", 1);
        UIManager.put("MenuBar.border", 0);

        centralPanel.setBorder(BorderFactory.createLineBorder(Color.white, 1));
        infoLabel.setBorder(BorderFactory.createLineBorder(Color.white, 1));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        build();
    }

    public void build() {
        Window window = this;
        int winWidth = 800;
        int winHeight = 800;

        setTitle("Game of Life");
        setSize(winWidth, winHeight);
        setLocationRelativeTo(null);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setVisible(true);

        /* Main Panel */
        JPanel mainPanel = new JPanel(new BorderLayout());
        setContentPane(mainPanel);

        /* Buttons Panel */
        JPanel downPanel = new JPanel(new BorderLayout());
        JPanel buttonsPanel = new JPanel(new FlowLayout());

        mainPanel.add(centralPanel, BorderLayout.CENTER);
        mainPanel.add(downPanel, BorderLayout.SOUTH);

        downPanel.add(buttonsPanel, BorderLayout.CENTER);
        downPanel.add(infoLabel, BorderLayout.WEST);

        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                environment.randomState();
            }
        });

        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if(scheduler.isPaused()) {
                    pauseButton.setText("Pause");
                } else {
                    pauseButton.setText("Play");
                }

                scheduler.togglePause();
            }
        });

        JCheckBox outlinesCheckbox = new JCheckBox("Outlines", true);
        outlinesCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                centralPanel.toggleOutlines();
                window.repaint();
            }
        });

        buttonsPanel.add(resetButton);
        buttonsPanel.add(pauseButton);
        buttonsPanel.add(outlinesCheckbox);
        buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.white, 1));

        /* Menu */
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem itemLoad = new JMenuItem("Load");

        setJMenuBar(menuBar);
        menuBar.add(menu);
        menu.add(itemLoad);

        /* Events */
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                super.keyPressed(event);

                int key = event.getKeyCode();

                switch(key) {
                    case KeyEvent.VK_ESCAPE:
                        dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
                        break;
                    case KeyEvent.VK_SPACE:
                        scheduler.togglePause();
                        pauseButton.setText(scheduler.isPaused() ? "Play" : "Pause");
                        pauseButton.repaint();
                        break;
                    case KeyEvent.VK_Z:
                    case KeyEvent.VK_UP:
                        centralPanel.move(HexaPanel.MoveDirection.up);
                        break;
                    case KeyEvent.VK_S:
                    case KeyEvent.VK_DOWN:
                        centralPanel.move(HexaPanel.MoveDirection.down);
                        break;
                    case KeyEvent.VK_Q:
                    case KeyEvent.VK_LEFT:
                        centralPanel.move(HexaPanel.MoveDirection.left);
                        break;
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        centralPanel.move(HexaPanel.MoveDirection.right);
                        break;
                    default:
                        break;
                }

                window.repaint();
            }
        });

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                super.mouseWheelMoved(event);

                if(event.getWheelRotation() < 0) {
                    centralPanel.zoomIn();
                } else {
                    centralPanel.zoomOut();
                }

                window.repaint();
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        centralPanel.repaint();
        infoLabel.setText(" Generation " + environment.getGeneration() +
                                  " | Zoom: " + centralPanel.getZoom() + "x ");
    }
}
