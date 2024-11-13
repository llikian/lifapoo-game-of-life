package view_controller;

import model.Environment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Graphics;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

import javax.swing.border.Border;

public class Window extends JFrame implements Observer {
    private JPanel[][] panels;
    private Environment environment;

    public Color backgroundColor;
    public Color foregroundColor;
    public Color backgroundBrighter;

    public Window(Environment environment) {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.environment = environment;
        this.backgroundColor = new Color(30, 30, 30);
        this.foregroundColor = new Color(Color.white.getRGB());
        this.backgroundBrighter = new Color(50, 50, 50);
        build();
    }

    public void build() {
        setTitle("Game of Life");
        setSize(600, 600);
        setLocationRelativeTo(null);

        // Main Panel
        JPanel pan = new JPanel(new BorderLayout());

        // Central Panel
        JComponent pan1 = new JPanel(new GridLayout(environment.getWidth(), environment.getHeight()));
        panels = new JPanel[environment.getWidth()][environment.getHeight()];

        pan1.setBackground(backgroundColor);
        pan1.setForeground(foregroundColor);
        pan1.setBorder(BorderFactory.createLineBorder(Color.white, 1));

        for(int i = 0 ; i < environment.getWidth() ; i++) {
            for(int j = 0 ; j < environment.getHeight() ; j++) {
                panels[i][j] = new JPanel() {
                    public void paintComponent(Graphics graphics) {
                        super.paintComponent(graphics);

                        int width = getWidth() / 2;
                        int height = getHeight() / 2;
                        int radius = width / 2;

                        graphics.setColor(Color.red);
                        graphics.fillOval(width - radius, height - radius, radius * 2, radius * 2);
                    }
                };
                panels[i][j].setBackground(backgroundColor);
                panels[i][j].setForeground(foregroundColor);
                panels[i][j].setVisible(environment.getState(i, j));

                pan1.add(panels[i][j]);
            }
        }

        // Buttons Panel
        JPanel pan2 = new JPanel(new FlowLayout());
        pan2.add(new JButton("b1"));
        pan2.add(new JTextField("jt1"));
        pan2.setBackground(backgroundColor);
        pan2.setForeground(foregroundColor);
        pan2.setBorder(BorderFactory.createLineBorder(Color.white, 1));

        pan.add(pan1, BorderLayout.CENTER);
        pan.add(pan2, BorderLayout.EAST);

        setContentPane(pan);

        // Menu
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
        for(int i = 0 ; i < environment.getWidth() ; i++) {
            for(int j = 0 ; j < environment.getHeight() ; j++) {
                panels[i][j].setVisible(environment.getState(i, j));
            }
        }
    }
}
