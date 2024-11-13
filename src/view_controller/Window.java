package view_controller;

import model.Environment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

import javax.swing.border.Border;

public class Window extends JFrame implements Observer {
    private JPanel[][] panels;
    private Environment environment;

    public Window(Environment environment) {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.environment = environment;
        build();
    }

    public void build() {
        setTitle("Game of Life");
        setSize(600, 600);
        setLocationRelativeTo(null);

        Color backgroundColor = new Color(30, 30, 30);
        Color textColor = Color.white;

        // Main Panel
        JPanel pan = new JPanel(new BorderLayout());

        // Central Panel
        JComponent pan1 = new JPanel(new GridLayout(environment.getWidth(), environment.getHeight()));
        panels = new JPanel[environment.getWidth()][environment.getHeight()];

        pan1.setBackground(backgroundColor);
        pan1.setForeground(textColor);
        pan1.setBorder(BorderFactory.createLineBorder(Color.white, 1));

        for(int i = 0 ; i < environment.getWidth() ; i++) {
            for(int j = 0 ; j < environment.getHeight() ; j++) {
                panels[i][j] = new JPanel();

                pan1.add(panels[i][j]);
            }
        }

        // Buttons Panel
        JPanel pan2 = new JPanel(new FlowLayout());
        pan2.add(new JButton("b1"));
        pan2.add(new JTextField("jt1"));
        pan2.setBackground(backgroundColor);
        pan2.setForeground(textColor);
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

        menuBar.setBackground(new Color(50, 50, 50));
        menuBar.setForeground(textColor);
        menuBar.setBorder(null);
        menu.setBackground(backgroundColor);
        menu.setForeground(textColor);
        itemLoad.setBackground(backgroundColor);
        itemLoad.setForeground(textColor);
    }

    @Override
    public void update(Observable o, Object arg) {
        for(int i = 0 ; i < environment.getWidth() ; i++) {
            for(int j = 0 ; j < environment.getHeight() ; j++) {
                if(environment.getState(i, j)) {
                    panels[i][j].setBackground(Color.red);
                } else {
                    panels[i][j].setBackground(new Color(50, 50, 50));
                }
            }
        }
    }
}
