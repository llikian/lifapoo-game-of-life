package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Simulates an implementation of the Game of Life.
 * Attributes:
 * - cells: a 2D array of cells
 * - width: the width of the grid
 * - height: the height of the grid
 * - generation: the current generation's index
 */
public class Environment extends Observable implements Runnable {
    private Cell[][] cells;
    private HashMap<Cell, Point> hashmap;
    private int width;
    private int height;
    private int generation;

    /**
     * Initializes the entire grid with random states.
     *
     * @param width  The width of the grid.
     * @param height The height of the grid.
     */
    public Environment(int width, int height) {
        this.cells = new Cell[width][height];
        this.hashmap = new HashMap<>();
        this.width = width;
        this.height = height;
        this.generation = 1;

        for(int i = 0 ; i < width ; i++) {
            for(int j = 0 ; j < height ; j++) {
                cells[i][j] = new Cell(this);
                hashmap.put(cells[i][j], new Point(i, j));
            }
        }

        try {
            saveToFile("data/test.gol");
        } catch(java.io.IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Loads an environment from the data contained in a file.
     *
     * @param path The path to the file containing the data.
     */
    public void loadFromFile(String path) throws FileNotFoundException {
        File file = new File(path);
        Scanner scanner = new Scanner(file);

        width = scanner.nextInt();
        height = scanner.nextInt();
        generation = scanner.nextInt();

        int c;
        for(int i = 0 ; i < width ; i++) {
            for(int j = 0 ; j < height ; j++) {
                c = scanner.nextInt();
                cells[i][j].setState(c == 1);
            }
        }

        scanner.close();
    }

    /**
     * Saves the environment's state to a file.
     *
     * @param path The path at which the file will be saved.
     */
    public void saveToFile(String path) throws IOException {
        File file = new File(path);
        if(!file.createNewFile()) {
            file.delete();
        }

        FileWriter writer = new FileWriter(path);

        writer.write(String.valueOf(width) + ' ');
        writer.write(String.valueOf(height) + '\n');
        writer.write(String.valueOf(generation) + '\n');
        writer.write('\n');

        for(int i = 0 ; i < width ; i++) {
            for(int j = 0 ; j < height ; j++) {
                writer.write(cells[i][j].getState() ? '1' : '0');
                writer.write(' ');
            }

            writer.write('\n');
        }

        writer.close();
    }

    /**
     * The amount of cells in a line of the grid.
     *
     * @return The grid's width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * The amount of cells in a column of the grid.
     *
     * @return The grid's height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * The current state of a specific cell.
     *
     * @param x The cell's x position in the grid.
     * @param y The cell's y position in the grid.
     * @return The cell's state.
     */
    public boolean getState(int x, int y) {
        return cells[x][y].getState();
    }

    /**
     * The amount of alive neighbouring cells of a specific cell.
     *
     * @param x The cell's x position in the grid.
     * @param y The cell's y position in the grid.
     * @return The amount of alive neighbouring cells of the cell.
     */
    public int getAliveCount(int x, int y) {
        return cells[x][y].getAliveCount();
    }

    /**
     * The current generation of the Game of Life.
     *
     * @return The current generation.
     */
    public int getGeneration() {
        return generation;
    }

    /**
     * Toggles the state of a specific cell.
     *
     * @param x The cell's x position in the grid.
     * @param y The cell's y position in the grid.
     */
    public void toggleState(int x, int y) {
        cells[x][y].toggleState();

        setChanged();
        notifyObservers();
    }

    /**
     * Finds the cell that is in the specified direction from another.
     *
     * @param source    The cell from which we start.
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

        return cells[(pos.x + shift.x + width) % width][(pos.y + shift.y + height) % height];
    }

    /**
     * Fills the entire grid with random cells.
     */
    public void randomState() {
        generation = 1;

        for(int i = 0 ; i < width ; i++) {
            for(int j = 0 ; j < height ; j++) {
                cells[i][j].randomState();
                cells[i][j].counted = false;
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
                cells[i][j].counted = false;
            }
        }

        for(int i = 0 ; i < width ; i++) {
            for(int j = 0 ; j < height ; j++) {
                cells[i][j].flip();
            }
        }
    }

    /**
     * Kills the entire colony.
     */
    public void genocide() {
        generation = 1;

        for(int i = 0 ; i < width ; i++) {
            for(int j = 0 ; j < height ; j++) {
                cells[i][j].kill();
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
