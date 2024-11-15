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

    public Color backgroundColor;
    public Color foregroundColor;
    public Color backgroundBrighter;
    public Color selectionBackground;

    public Window(Environment environment, Scheduler scheduler) {
        super();

        this.environment = environment;
        this.scheduler = scheduler;
        this.centralPanel = new HexaPanel(environment);

        this.backgroundColor = new Color(30, 30, 30);
        this.foregroundColor = Color.white;
        this.backgroundBrighter = new Color(50, 50, 50);
        this.selectionBackground = new Color(163, 29, 29);

        /* Background Colors */
        UIManager.put("Panel.background", backgroundColor);
        UIManager.put("Button.background", backgroundBrighter);
        UIManager.put("MenuBar.background", backgroundBrighter);
        UIManager.put("Menu.background", backgroundBrighter);
        UIManager.put("MenuItem.background", backgroundBrighter);
        UIManager.put("PopupMenu.background", backgroundColor);

        /* Foreground Colors */
        UIManager.put("Panel.foreground", foregroundColor);
        UIManager.put("Button.foreground", foregroundColor);
        UIManager.put("MenuBar.foreground", foregroundColor);
        UIManager.put("Menu.foreground", foregroundColor);
        UIManager.put("MenuItem.foreground", foregroundColor);

        /* Selected / Hovered Background Colors */
        UIManager.put("Menu.selectionBackground", selectionBackground);
        UIManager.put("MenuItem.selectionBackground", selectionBackground);
        UIManager.put("Button.selectionBackground", selectionBackground);

        /* Selected / Hovered Foreground Colors */
        UIManager.put("Menu.selectionForeground", foregroundColor);
        UIManager.put("MenuItem.selectionForeground", foregroundColor);
        UIManager.put("Button.selectionForeground", foregroundColor);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        build();
    }

    public void build() {
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

        /* Central Panel */
        centralPanel.setBorder(BorderFactory.createLineBorder(Color.white, 1));
        centralPanel.setBackground(backgroundColor);

        /* Buttons Panel */
        JPanel buttonsPanel = new JPanel(new FlowLayout());

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
            public void actionPerformed(ActionEvent actionEvent) {
                if(scheduler.isPaused()) {
                    pauseButton.setText("Pause");
                } else {
                    pauseButton.setText("Play");
                }

                scheduler.togglePause();
            }
        });

        buttonsPanel.add(resetButton);
        buttonsPanel.add(pauseButton);
        buttonsPanel.setBorder(BorderFactory.createLineBorder(Color.white, 1));

        mainPanel.add(centralPanel, BorderLayout.CENTER);
        mainPanel.add(buttonsPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);

        /* Menu */
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem itemLoad = new JMenuItem("Load");

        setJMenuBar(menuBar);
        menuBar.add(menu);
        menu.add(itemLoad);

        menuBar.setBorder(null);

        /* Key Events */
        Window window = this;

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
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
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        centralPanel.repaint();
    }
}
