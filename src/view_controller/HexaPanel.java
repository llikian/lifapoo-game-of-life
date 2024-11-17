package view_controller;

import model.Environment;

import javax.swing.*;
import java.awt.*;

public class HexaPanel extends JPanel {
    private final Environment environment;
    private final double[][] hexagon;
    private Color[] cellColors;

    private final double zoomRate;
    private double zoom;

    public enum MoveDirection { up, down, left, right };
    private final double sensitivity;
    private double originX;
    private double originY;

    private boolean outlines;

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

        this.hexagon[0][0] = 0.866025;
        this.hexagon[0][1] = 0.500000;
        this.hexagon[1][0] = 0.000000;
        this.hexagon[1][1] = 1.000000;
        this.hexagon[2][0] = -0.866025;
        this.hexagon[2][1] = 0.500000;
        this.hexagon[3][0] = -0.866025;
        this.hexagon[3][1] = -0.500000;
        this.hexagon[4][0] = -0.000000;
        this.hexagon[4][1] = -1.000000;
        this.hexagon[5][0] = 0.866025;
        this.hexagon[5][1] = -0.500000;

        this.cellColors[0] = new Color(0xFFEAE6CC, true);
        this.cellColors[1] = new Color(0xDAF7A6);
        this.cellColors[2] = new Color(0xFFC300);
        this.cellColors[3] = new Color(0xFF5733);
        this.cellColors[4] = new Color(0xC70039);
        this.cellColors[5] = new Color(0x900C3F);
        this.cellColors[6] = new Color(0x581845);
    }

    public void toggleOutlines() {
        outlines = !outlines;
    }

    public double getZoom() {
        return zoom;
    }

    public void zoomIn()  {
        if(zoom < 10.0) {
            zoom += zoomRate;
        }
    }

    public void zoomOut()  {
        if(zoom > zoomRate) {
            zoom -= zoomRate;
        }
    }

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

    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        int width = getWidth();
        int height = getHeight();

        double r = zoom * 0.55 * width / environment.getWidth();
        double shiftX = 2.0 * Math.sqrt(0.75 * r * r);
        double shiftY = Math.sqrt(0.75 * shiftX * shiftX);

        double totalW = (environment.getWidth() + 1.5) * shiftX;
        double totalH = (environment.getHeight() + 1) * shiftY;

        Polygon hex = new Polygon();
        double[][] origins = new double[6][2];
        for(int k = 0 ; k < 6 ; ++k) {
            hex.addPoint(0, 0);
            origins[k][0] = (width - totalW) / 2.0 + r * hexagon[k][0];
            origins[k][1] = (height - totalH) / 2.0 + r * hexagon[k][1];
        }

        for(int i = 0 ; i < environment.getWidth() ; i++) {
            for(int j = 0 ; j < environment.getHeight() ; j++) {
                double shift = (j % 2 == 0) ? 0.0 : shiftX / 2.0;

                for(int k = 0 ; k < 6 ; k++) {
                    hex.xpoints[k] = (int) (origins[k][0] + (i + 1) * shiftX + shift + originX);
                    hex.ypoints[k] = (int) (origins[k][1] + (j + 1) * shiftY + originY);
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
