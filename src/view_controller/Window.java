package view_controller;

import model.Environment;

import java.awt.*;

import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

public class Window extends JFrame implements Observer {
    private JPanel centralPanel;

    public Color backgroundColor;
    public Color foregroundColor;
    public Color backgroundBrighter;

    public Window(Environment environment) {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.centralPanel = new HexaPanel(environment);
        this.backgroundColor = new Color(30, 30, 30);
        this.foregroundColor = new Color(Color.white.getRGB());
        this.backgroundBrighter = new Color(50, 50, 50);

        build();
    }

    public void build() {
        int winWidth = 800;
        int winHeight = 800;

        setTitle("Game of Life");
        setSize(winWidth, winHeight);
        setLocationRelativeTo(null);

        /* Main Panel */
        JPanel pan = new JPanel(new BorderLayout());

        /* Central Panel */
        centralPanel.setBackground(backgroundColor);
        centralPanel.setBorder(BorderFactory.createLineBorder(Color.white, 1));

        /* Buttons Panel */
        JPanel pan2 = new JPanel(new FlowLayout());
        pan2.add(new JButton("b1"));
        pan2.add(new JTextField("jt1"));
        pan2.setBackground(backgroundColor);
        pan2.setForeground(foregroundColor);
        pan2.setBorder(BorderFactory.createLineBorder(Color.white, 1));

        pan.add(centralPanel, BorderLayout.CENTER);
        pan.add(pan2, BorderLayout.EAST);

        setContentPane(pan);

        /* Menu */
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenuItem itemLoad = new JMenuItem("Load");
        menu.add(itemLoad);
        menuBar.add(menu);
        setJMenuBar(menuBar);

        menuBar.setBackground(backgroundBrighter);
        menuBar.setForeground(foregroundColor);
        menuBar.setBorder(null);
        menu.setBackground(backgroundColor);
        menu.setForeground(foregroundColor);
        itemLoad.setBackground(backgroundColor);
        itemLoad.setForeground(foregroundColor);
    }

    @Override
    public void update(Observable o, Object arg) {
        centralPanel.repaint();
    }
}
