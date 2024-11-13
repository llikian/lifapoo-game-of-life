package model;

import java.util.Observable;
import java.util.HashMap;

public class Environment extends Observable implements Runnable {
    private Cell[][] cells;
    HashMap<Cell, Point> hashmap;
    private int width;
    private int height;

    public Environment(int width, int height) {
        cells = new Cell[width][height];
        hashmap = new HashMap<Cell, Point>();
        this.width = width;
        this.height = height;

        for(int i = 0 ; i < width ; i++) {
            for(int j = 0 ; j < height ; j++) {
                cells[i][j] = new Cell(this);
                hashmap.put(cells[i][j], new Point(i, j));
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean getState(int x, int y) {
        return cells[x][y].getState();
    }

    public Cell getCell(Cell source, Direction direction) {
        Point pos = hashmap.get(source);

        switch(direction) {
            case upL:
                return cells[(pos.x - 1 + width) % width][(pos.y - 1 + height) % height];
            case up:
                return cells[(pos.x + width) % width][(pos.y - 1 + height) % height];
            case upR:
                return cells[(pos.x + 1 + width) % width][(pos.y - 1 + height) % height];
            case left:
                return cells[(pos.x - 1 + width) % width][(pos.y + height) % height];
            case right:
                return cells[(pos.x + 1 + width) % width][(pos.y + height) % height];
            case downL:
                return cells[(pos.x - 1 + width) % width][(pos.y + 1 + height) % height];
            case down:
                return cells[(pos.x + width) % width][(pos.y + 1 + height) % height];
            case downR:
                return cells[(pos.x + 1 + width) % width][(pos.y + 1 + height) % height];
            default: /* Should not be reached */
                return null;
        }
    }

    public void randomState() {
        for(int i = 0 ; i < width ; i++) {
            for(int j = 0 ; j < height ; j++) {
                cells[i][j].randomState();
            }
        }
    }

    public void nextState() {
        for(int i = 0 ; i < width ; i++) {
            for(int j = 0 ; j < height ; j++) {
                cells[i][j].nextState();
            }
        }
    }

    @Override
    public void run() {
        nextState();
        setChanged();
        notifyObservers();
    }
}
