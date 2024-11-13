package model;

import java.util.Observable;

public class Environment extends Observable implements Runnable {
    public Environment(int width, int height) {
        this.width = width;
        this.height = height;

        cells = new Cell[width][height];
        for(int i = 0 ; i < width ; i++) {
            for(int j = 0 ; j < height ; j++) {
                cells[i][j] = new Cell(this);
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
        // TODO : une case utilisera ogligatoirement cette fonction pour percevoir son environnement, et définir son état suivant

        return null;
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

    private Cell[][] cells;
    private int width;
    private int height;
}
