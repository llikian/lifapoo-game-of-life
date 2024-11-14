package view_controller;

import model.Environment;

import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

public class Window extends JFrame implements Observer {
    private Environment environment;
    private HexaPanel centralPanel;

    private boolean pause;

    public Color backgroundColor;
    public Color foregroundColor;
    public Color backgroundBrighter;
    public Color selectionColor;
    public Color selectionForeground;

    public Window(Environment environment) {
        super();

        this.environment = environment;
        this.centralPanel = new HexaPanel(environment);
        this.pause = false;
        this.backgroundColor = new Color(30, 30, 30);
        this.foregroundColor = new Color(Color.white.getRGB());
        this.backgroundBrighter = new Color(50, 50, 50);
        this.selectionColor = new Color(163, 29, 29);

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
        UIManager.put("Menu.selectionBackground", selectionColor);
        UIManager.put("MenuItem.selectionBackground", selectionColor);
        UIManager.put("Button.selectionBackground", selectionColor);

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

        /* Main Panel */
        JPanel mainPanel = new JPanel(new BorderLayout());

        /* Central Panel */
        centralPanel.setBorder(BorderFactory.createLineBorder(Color.white, 1));
        centralPanel.setBackground(backgroundColor);

        /* Buttons Panel */
        JPanel buttonsPanel = new JPanel(new FlowLayout());

        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                environment.randomState();
            }
        });

        JButton pauseButton = new JButton("Pause");
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(pause) {
                    pauseButton.setText("Pause");
                    pause = false;
                } else {
                    pauseButton.setText("Play");
                    pause = true;
                }
            }
        });

        buttonsPanel.add(restartButton);
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
    }

    @Override
    public void update(Observable o, Object arg) {
        centralPanel.repaint();
    }
}
