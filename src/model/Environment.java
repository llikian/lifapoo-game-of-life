package model;

import java.util.Observable;
import java.util.HashMap;

public class Environment extends Observable implements Runnable {
    private Cell[][] cells;
    private Cell[][] oldCells;
    private HashMap<Cell, Point> hashmap;
    private int width;
    private int height;

    public Environment(int width, int height) {
        this.cells = new Cell[width][height];
        this.oldCells = new Cell[width][height];
        this.hashmap = new HashMap<>();
        this.width = width;
        this.height = height;

        for(int i = 0 ; i < width ; i++) {
            for(int j = 0 ; j < height ; j++) {
                cells[i][j] = new Cell(this);
                hashmap.put(cells[i][j], new Point(i, j));
                oldCells[i][j] = new Cell(cells[i][j]);
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

    public int getAliveCount(int x, int y) {
        return cells[x][y].getAliveCount();
    }

    public Cell getCell(Cell source, Direction direction) {
        Point pos = hashmap.get(source);
        Point shift = new Point(0, 0);

        switch(direction) {
            case upL:
                shift.x = (pos.y % 2 == 0) ? -1 : 0;
                shift.y = -1;
                break;
            case upR:
                shift.x = (pos.y % 2 == 0) ? 0 : 1;
                shift.y = -1;
                break;
            case left:
                shift.x = -1;
                shift.y = 0;
                break;
            case right:
                shift.x = 1;
                shift.y = 0;
                break;
            case downL:
                shift.x = (pos.y % 2 == 0) ? -1 : 0;
                shift.y = 1;
                break;
            case downR:
                shift.x = (pos.y % 2 == 0) ? 0 : 1;
                shift.y = 1;
                break;
        }

        return oldCells[(pos.x + shift.x + width) % width][(pos.y + shift.y + height) % height];
    }

    public void randomState() {
        for(int i = 0 ; i < width ; i++) {
            for(int j = 0 ; j < height ; j++) {
                cells[i][j].randomState();
                oldCells[i][j] = new Cell(cells[i][j]);
            }
        }
    }

    public void nextState() {
        for(int i = 0 ; i < width ; i++) {
            for(int j = 0 ; j < height ; j++) {
                cells[i][j].nextState();
            }
        }

        for(int i = 0 ; i < width ; i++) {
            for(int j = 0 ; j < height ; j++) {
                oldCells[i][j] = new Cell(cells[i][j]);
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
