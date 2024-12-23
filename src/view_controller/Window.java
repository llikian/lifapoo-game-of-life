package view_controller;

import model.Environment;
import model.Scheduler;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.Observable;
import java.util.Observer;

/**
 * A class representing the current window.
 * <p>
 * Attributes:<br>
 * - Environment: a reference to the current environment.<br>
 * - Scheduler: a reference to the scheduler.<br>
 * - centralPanel: a reference to the HexaPanel used to represent the game.<br>
 * - infoLabel: a Label displaying useful information to the user.<br>
 * - mainPanel: The panel used to display the HexaPanel.<br>
 * - downPanel: a panel to the bottom of the screen.<br>
 * - buttonsPanel: a panel filled with a bunch of buttons.<br>
 * - pauseButton: a pause button.
 */
public class Window extends JFrame implements Observer {
    private final Environment environment;
    private final Scheduler scheduler;
    private final HexaPanel centralPanel;
    private final JPanel mainPanel;
    private final JPanel downPanel;
    private final JPanel buttonsPanel;
    private final JButton pauseButton;
    private final JLabel infoLabel;
    private final JSlider speedSlider;

    /**
     * @param environment A reference to the current environment.
     * @param scheduler   A reference to the current scheduler.
     */
    public Window(Environment environment, Scheduler scheduler) {
        super();

        initStyle();

        this.environment = environment;
        this.scheduler = scheduler;
        this.centralPanel = new HexaPanel(environment);
        this.mainPanel = new JPanel(new BorderLayout());
        this.downPanel = new JPanel(new BorderLayout());
        this.buttonsPanel = new JPanel(new FlowLayout());
        this.pauseButton = new JButton("Pause");
        this.infoLabel = new JLabel("");
        this.speedSlider = new JSlider(20, 2000, (int) scheduler.getSleepTime());

        initWindow();
        initBorders();
        initButtonsPanel();
        initMenuBar();
        handleKeyEvents();
        handleWheelEvents();
    }

    /**
     * The init function used to set up the screen style.
     */
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
        UIManager.put("Slider.background", backgroundColor);

        /* Foreground Colors */
        UIManager.put("Panel.foreground", foregroundColor);
        UIManager.put("Label.foreground", foregroundColor);
        UIManager.put("Button.foreground", foregroundColor);
        UIManager.put("CheckBox.foreground", foregroundColor);
        UIManager.put("MenuBar.foreground", foregroundColor);
        UIManager.put("Menu.foreground", foregroundColor);
        UIManager.put("MenuItem.foreground", foregroundColor);
        UIManager.put("Slider.foreground", foregroundColor);

        /* Selected / Hovered Background Colors */
        UIManager.put("Menu.selectionBackground", selectionBackground);
        UIManager.put("MenuItem.selectionBackground", selectionBackground);
        UIManager.put("Button.selectionBackground", selectionBackground);

        /* Selected / Hovered Foreground Colors */
        UIManager.put("Menu.selectionForeground", foregroundColor);
        UIManager.put("MenuItem.selectionForeground", foregroundColor);
        UIManager.put("Button.selectionForeground", foregroundColor);

        /* Border Color */
        UIManager.put("Panel.borderColor", Color.white);
        UIManager.put("MenuBar.borderColor", Color.white);

        /* Border Thickness */
        UIManager.put("Panel.border", 1);
        UIManager.put("MenuBar.border", 0);
    }

    /**
     * The window initializer.
     */
    private void initWindow() {
        setTitle("Game of Life");
        setSize(1400, 800);
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

    /**
     * The border initializer.
     */
    private void initBorders() {
        Color borderColor = new Color(0xFFDFC1);
        int borderWidth = 1;

        centralPanel.setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));
        infoLabel.setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));
        buttonsPanel.setBorder(BorderFactory.createLineBorder(borderColor, borderWidth));
    }

    /**
     * The initializer for the button panel.
     */
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

        /* Genocide Button */
        JButton genocideButton = new JButton("Genocide");
        genocideButton.setFocusable(false);
        buttonsPanel.add(genocideButton);

        genocideButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                environment.genocide();
                centralPanel.repaint();
                repaintInfoLabel();
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

        /* Speed Time Text Field */
        speedSlider.setFocusable(false);
        buttonsPanel.add(speedSlider);
    }

    /**
     * The menuBar initializer.
     */
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu menu = new JMenu("File");
        menuBar.add(menu);

        JMenuItem itemLoad = new JMenuItem("Load");
        menu.add(itemLoad);
        itemLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    JFileChooser chooser = new JFileChooser(
                            System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() + "data");

                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Game Of Life data files (.gol)", "gol");
                    chooser.setFileFilter(filter);

                    if(chooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                        environment.loadFromFile(chooser.getSelectedFile().getPath());
                    }

                    centralPanel.repaint();
                } catch(FileNotFoundException exception) {
                    throw new RuntimeException(exception);
                }
            }
        });

        JMenuItem itemSave = new JMenuItem("Save");
        menu.add(itemSave);
        itemSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    JFileChooser chooser = new JFileChooser(
                            System.getProperty("user.dir") + FileSystems.getDefault().getSeparator() + "data");

                    FileNameExtensionFilter filter = new FileNameExtensionFilter("Game Of Life data files (.gol)", "gol");
                    chooser.setFileFilter(filter);

                    if(chooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION) {
                        File file = chooser.getSelectedFile();
                        String filename = file.getName();

                        int index = filename.lastIndexOf(".");

                        if(index > 0 && filename.substring(index + 1).equalsIgnoreCase("gol")) {
                            environment.saveToFile(file.getPath());
                        } else {
                            environment.saveToFile(file.getPath() + ".gol");
                        }
                    }
                } catch(IOException exception) {
                    throw new RuntimeException(exception);
                }
            }
        });
    }

    /**
     * The controller used by the keyboard.
     */
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

    /**
     * The controller for the mouse.
     */
    private void handleWheelEvents() {
        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                super.mouseWheelMoved(event);

                repaintInfoLabel();
            }
        });
    }

    /**
     * A function to force the infolabel to be repainted.
     */
    private void repaintInfoLabel() {
        String text = " ";
        text += "Generation " + environment.getGeneration();
        text += " | ";
        text += "Zoom: " + centralPanel.getScale() + 'x';
        text += " | ";
        text += "Refresh Rate: " + scheduler.getSleepTime() + "ms";
        text += ' ';

        infoLabel.setText(text);
    }

    /**
     * The controller to force the game to be paused.
     */
    private void togglePause() {
        scheduler.togglePause();
        pauseButton.setText(scheduler.isPaused() ? "Play" : "Pause");
    }

    /**
     * @param o   The observable object.
     * @param arg An argument passed to the {@code notifyObservers} method.
     */
    @Override
    public void update(Observable o, Object arg) {
        centralPanel.repaint();
        repaintInfoLabel();
        scheduler.setSleepTime(speedSlider.getValue());
    }
}
