package view_controller;

import model.Environment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

import javax.swing.border.Border;

public class Window extends JFrame implements Observer {
    private JPanel[][] panels;
    Environment environment;

    public Window(Environment environment) {
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.environment = environment;
        build();
    }

    public void build() {
        setTitle("Game of Life");
        setSize(600, 600);

        // Main Panel
        JPanel pan = new JPanel(new BorderLayout());

        // Central Panel
        JComponent pan1 = new JPanel(new GridLayout(environment.getWidth(), environment.getHeight()));
        panels = new JPanel[environment.getWidth()][environment.getHeight()];

        Border blackline = BorderFactory.createLineBorder(Color.black, 1);
        pan1.setBorder(blackline);
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

        pan.add(pan1, BorderLayout.CENTER);
        pan.add(pan2, BorderLayout.EAST);

        setContentPane(pan);

        // Menu
        JMenuBar jm = new JMenuBar();
        JMenu m = new JMenu("Fichier");
        JMenuItem mi = new JMenuItem("Charger");
        m.add(mi);
        jm.add(m);
        setJMenuBar(jm);
    }

    @Override
    public void update(Observable o, Object arg) {
        for(int i = 0 ; i < environment.getWidth() ; i++) {
            for(int j = 0 ; j < environment.getHeight() ; j++) {
                if(environment.getState(i, j)) {
                    panels[i][j].setBackground(Color.BLACK);
                } else {
                    panels[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }
}
