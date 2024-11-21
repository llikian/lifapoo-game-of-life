package view_controller;

import model.Environment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @class HexaPanel
 * @brief This class is used to represent the panel of Hexagons, representing the game of life.
 * Attributes:
 * - Environment: A reference to the current environment
 * - hexagon: a 2D array representing the panel to draw
 * - cellColors: a 1D array representing the colors of every cell
 * - zoomRate: the step between each zoom/de-zoom
 * - zoom: the multiplier used to zoom/de-zoom functionality
 * - MoveDirection: an enumeration of possible movement on the panel
 * - sensitivity: the step between two movement on the panel
 * - originX: the current position of the X axis origin
 * - originY: the current position of the Y axis origin
 * - outlines: Used to display or not the outlines of each cell
 */
public class HexaPanel extends JPanel {
    private final Environment environment;
    private final double[][] hexagon;
    private Color[] cellColors;

    private final double zoomRate;
    private double zoom;

    public enum MoveDirection {up, down, left, right}

    ;
    private final double sensitivity;
    private double originX;
    private double originY;

    private boolean outlines;

    private double radius;

    private double shiftX;
    private double shiftY;

    private double totalW;
    private double totalH;

    private double[][] origins;

    /**
     * @param environment The current environment we want to use
     */
    public HexaPanel(Environment environment) {
        this.environment = environment;
        this.hexagon = new double[6][2];
        this.cellColors = new Color[7];

        this.zoomRate = 0.5;
        this.zoom = 1.0;

        this.sensitivity = 10.0;
        this.originX = 0.0;
        this.originY = 0.0;

        this.outlines = true;

        this.hexagon[0][0] = 0.86602540378443864676372317;
        this.hexagon[0][1] = 0.5;
        this.hexagon[1][0] = 0.0;
        this.hexagon[1][1] = 1.0;
        this.hexagon[2][0] = -0.86602540378443864676372317;
        this.hexagon[2][1] = 0.5;
        this.hexagon[3][0] = -0.86602540378443864676372317;
        this.hexagon[3][1] = -0.5;
        this.hexagon[4][0] = -0.0;
        this.hexagon[4][1] = -1.0;
        this.hexagon[5][0] = 0.86602540378443864676372317;
        this.hexagon[5][1] = -0.5;

        this.cellColors[0] = new Color(0xFFEAE6);
        this.cellColors[1] = new Color(0xFFCC00);
        this.cellColors[2] = new Color(0xFF9933);
        this.cellColors[3] = new Color(0xF65F28);
        this.cellColors[4] = new Color(0xE60909);
        this.cellColors[5] = new Color(0x900C20);
        this.cellColors[6] = new Color(0x58182B);

        this.origins = new double[6][2];

        update();

        /* Resize Listener */
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent event) {
                super.componentResized(event);

                update();
            }
        });

        /* Mouse Listener */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                super.mouseClicked(event);

                if(event.getButton() == MouseEvent.BUTTON1) {
                    double x = event.getX();
                    double y = event.getY();

                    int width = getWidth();
                    int height = getHeight();

                    y = Math.round((y - originY - (height - totalH) / 2.0) / shiftY - 1.0);
                    x = (x - originX - (width - totalW) / 2.0) / shiftX - (((int) y % 2 == 0) ? 1.0 : 1.5);

                    if(x < 0.0 || x > environment.getWidth() || y < 0.0 || y > environment.getHeight()) {
                        return;
                    }

                    environment.toggleState((int) Math.round(x), (int) y);
                }
            }
        });
    }

    /**
     * Updates all of the fields necessary for drawing the grid.
     */
    public void update() {
        int width = getWidth();
        int height = getHeight();

        radius = zoom * 0.55 * width / environment.getWidth();

        shiftX = 2.0 * Math.sqrt(0.75 * radius * radius);
        shiftY = Math.sqrt(0.75 * shiftX * shiftX);

        totalW = (environment.getWidth() + 1.5) * shiftX;
        totalH = (environment.getHeight() + 1.0) * shiftY;

        for(int k = 0 ; k < 6 ; ++k) {
            origins[k][0] = (width - totalW) / 2.0 + radius * hexagon[k][0];
            origins[k][1] = (height - totalH) / 2.0 + radius * hexagon[k][1];
        }
    }

    /**
     * Flips on/off the outline display
     */
    public void toggleOutlines() {
        outlines = !outlines;
    }

    /**
     * @return the current zoom
     */
    public double getZoom() {
        return zoom;
    }

    /**
     * Increase the zoom multiplier
     */
    public void zoomIn() {
        if(zoom < 10.0) {
            zoom += zoomRate;
        }

        update();
    }

    /**
     * Decrease the zoom multiplier
     */
    public void zoomOut() {
        if(zoom > zoomRate) {
            zoom -= zoomRate;
        }

        update();
    }

    /**
     * @param direction the direction to move the origin to
     */
    public void move(MoveDirection direction) {
        switch(direction) {
            case up:
                originY += sensitivity;
                break;
            case down:
                originY -= sensitivity;
                break;
            case left:
                originX += sensitivity;
                break;
            case right:
                originX -= sensitivity;
                break;
        }
    }

    /**
     * A custom method to paint specific component to the screen
     *
     * @param graphics the <code>Graphics</code> object to protect
     */
    @Override
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        Polygon hex = new Polygon();
        for(int k = 0 ; k < 6 ; ++k) {
            hex.addPoint(0, 0);
        }

        for(int i = 0 ; i < environment.getWidth() ; i++) {
            for(int j = 0 ; j < environment.getHeight() ; j++) {
                double shift = (j % 2 == 0) ? 0.0 : shiftX / 2.0;

                for(int k = 0 ; k < 6 ; k++) {
                    hex.xpoints[k] = (int) Math.round(origins[k][0] + (i + 1) * shiftX + shift + originX);
                    hex.ypoints[k] = (int) Math.round(origins[k][1] + (j + 1) * shiftY + originY);
                }

                if(environment.getState(i, j)) {
                    graphics.setColor(cellColors[environment.getAliveCount(i, j)]);
                    graphics.fillPolygon(hex);
                }

                if(outlines) {
                    graphics.setColor(Color.white);
                    graphics.drawPolygon(hex);
                }
            }
        }
    }
}
