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
    private final JPanel mainPanel;
    private final JPanel downPanel;
    private final JPanel buttonsPanel;
    private final JButton pauseButton;

    public Window(Environment environment, Scheduler scheduler) {
        super();

        initStyle();

        this.environment = environment;
        this.scheduler = scheduler;
        this.centralPanel = new HexaPanel(environment);
        this.infoLabel = new JLabel("");
        this.mainPanel = new JPanel(new BorderLayout());
        this.downPanel = new JPanel(new BorderLayout());
        this.buttonsPanel = new JPanel(new FlowLayout());
        this.pauseButton = new JButton("Pause");

        initWindow();
        initBorders();
        initButtonsPanel();
        initMenuBar();
        handleKeyEvents();
        handleWheelEvents();
    }

    private void initStyle() {
        Color backgroundColor = new Color(30, 30, 30);
        Color foregroundColor = new Color(0xFFDFC1);
        Color backgroundBrighter = new Color(50, 50, 50);
        Color selectionBackground = new Color(211, 56, 56);

        /* Background Colors */
        UIManager.put("Panel.background", backgroundColor);
        UIManager.put("Label.background", backgroundBrighter);
        UIManager.put("Button.background", backgroundBrighter);
        UIManager.put("CheckBox.background", backgroundBrighter);
        UIManager.put("MenuBar.background", backgroundBrighter);
        UIManager.put("Menu.background", backgroundBrighter);
        UIManager.put("MenuItem.background", backgroundBrighter);
        UIManager.put("PopupMenu.background", backgroundColor);

        /* Foreground Colors */
        UIManager.put("Panel.foreground", foregroundColor);
        UIManager.put("Label.foreground", foregroundColor);
        UIManager.put("Button.foreground", foregroundColor);
        UIManager.put("CheckBox.foreground", foregroundColor);
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

        /* Border Thickness */
        UIManager.put("Panel.borderColor", Color.white);
        UIManager.put("MenuBar.borderColor", Color.white);

        /* Border Color */
        UIManager.put("Panel.border", 1);
        UIManager.put("MenuBar.border", 0);
    }

    private void initWindow() {
        setTitle("Game of Life");
        setSize(800, 800);
        setLocationRelativeTo(null);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setContentPane(mainPanel);

        mainPanel.add(centralPanel, BorderLayout.CENTER);
        mainPanel.add(downPanel, BorderLayout.SOUTH);

        downPanel.add(buttonsPanel, BorderLayout.CENTER);
        downPanel.add(infoLabel, BorderLayout.WEST);
    }

    private void initBorders() {
        Color borderColor = new Color(0xFFDFC1);
        int borderWidth = 1;

        centralPanel.setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));
        infoLabel.setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));
        buttonsPanel.setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));
    }

    private void initButtonsPanel() {
        Window window = this;

        /* Reset Button */
        JButton resetButton = new JButton("Reset");
        resetButton.setFocusable(false);
        buttonsPanel.add(resetButton);

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                environment.randomState();
            }
        });

        /* Pause Button */
        pauseButton.setFocusable(false);
        buttonsPanel.add(pauseButton);

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                togglePause();
            }
        });

        /* Outlines Checkbox */
        JCheckBox outlinesCheckbox = new JCheckBox("Outlines", true);
        outlinesCheckbox.setFocusable(false);
        buttonsPanel.add(outlinesCheckbox);

        outlinesCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                centralPanel.toggleOutlines();
                window.repaint();
            }
        });
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem itemLoad = new JMenuItem("Load");

        setJMenuBar(menuBar);
        menuBar.add(menu);
        menu.add(itemLoad);
    }

    private void handleKeyEvents() {
        Window window = this;

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
                        togglePause();
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
    }

    private void handleWheelEvents() {
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                super.mouseWheelMoved(event);

                if(event.getWheelRotation() < 0) {
                    centralPanel.zoomIn();
                } else {
                    centralPanel.zoomOut();
                }

                centralPanel.repaint();
                repaintInfoLabel();
            }
        });
    }

    private void repaintInfoLabel() {
        String text = " ";
        text += "Generation " + environment.getGeneration();
        text += " | ";
        text += "Zoom: " + centralPanel.getZoom() + "x";
        text += ' ';

        infoLabel.setText(text);
    }

    private void togglePause() {
        scheduler.togglePause();
        pauseButton.setText(scheduler.isPaused() ? "Play" : "Pause");
    }

    @Override
    public void update(Observable o, Object arg) {
        centralPanel.repaint();
        repaintInfoLabel();
    }
}
