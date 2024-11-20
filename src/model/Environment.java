package model;

import java.util.Observable;
import java.util.HashMap;

/**
 * Simulates an implementation of the Game of Life.
 */
public class Environment extends Observable implements Runnable {
    private Cell[][] cells;
    private Cell[][] oldCells;
    private HashMap<Cell, Point> hashmap;
    private int width;
    private int height;
    private int generation;

    /**
     * Initializes the entire grid with random states.
     * @param width The width of the grid.
     * @param height The height of the grid.
     */
    public Environment(int width, int height) {
        this.cells = new Cell[width][height];
        this.oldCells = new Cell[width][height];
        this.hashmap = new HashMap<>();
        this.width = width;
        this.height = height;
        this.generation = 1;

        for(int i = 0 ; i < width ; i++) {
            for(int j = 0 ; j < height ; j++) {
                cells[i][j] = new Cell(this);
                hashmap.put(cells[i][j], new Point(i, j));
                oldCells[i][j] = new Cell(cells[i][j]);
            }
        }
    }

    /**
     * The amount of cells in a line of the grid.
     * @return The grid's width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * The amount of cells in a column of the grid.
     * @return The grid's height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * The current state of a specific cell.
     * @param x The cell's x position in the grid.
     * @param y The cell's y position in the grid.
     * @return The cell's state.
     */
    public boolean getState(int x, int y) {
        return cells[x][y].getState();
    }

    /**
     * The amount of alive neighbouring cells of a specific cell.
     * @param x The cell's x position in the grid.
     * @param y The cell's y position in the grid.
     * @return The amount of alive neighbouring cells of the cell.
     */
    public int getAliveCount(int x, int y) {
        return cells[x][y].getAliveCount();
    }

    /**
     * The current generation of the Game of Life.
     * @return The current generation.
     */
    public int getGeneration() {
        return generation;
    }

    /**
     * Finds the cell that is in the specified direction from another.
     * @param source The cell from which we start.
     * @param direction The direction in which the destination cell is located.
     * @return The destination cell.
     */
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

    /**
     * Fills the entire grid with random cells.
     */
    public void randomState() {
        generation = 1;

        for(int i = 0 ; i < width ; i++) {
            for(int j = 0 ; j < height ; j++) {
                cells[i][j].randomState();
                oldCells[i][j] = new Cell(cells[i][j]);
            }
        }

        setChanged();
        notifyObservers();
    }

    /**
     * Determines the next generation of the game of life.
     */
    public void nextState() {
        generation++;

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

    /**
     * Updates the game of life and notifies the view.
     */
    @Override
    public void run() {
        nextState();
        setChanged();
        notifyObservers();
    }
}
