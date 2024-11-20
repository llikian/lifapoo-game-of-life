package model;

import java.util.Random;

/**
 * A {@link Cell} is a wrapper for a boolean (a state) that contains functionality to simulate
 * Conway's Game of Life.
 * It isn't much use on its own and contains a reference to an {@link Environment} that will allow
 * to link the cells together for the simulation to function.
 *
 * Attributes:
 * - random: a reference to a Random numbers generator
 * - environment: a reference to the current environment
 * - state: the current state (alive/dead) of the cell
 * - counted: true if the cell has counted its neighbors
 * - alive: the number of alive neighbors
 */
public class Cell {
    private static final Random random = new Random();
    private final Environment environment;
    private boolean state;
    private boolean counted;
    private int alive;

    /**
     * Initializes a cell's fields. The initial state of a cell is random.
     * @param environment The environment the cell is a part of.
     */
    public Cell(Environment environment) {
        this.environment = environment;
        randomState();
        this.counted = false;
        this.alive = 0;
    }

    /**
     * Copy constructor for a cell.
     * @param cell The cell to copy the values from.
     */
    public Cell(Cell cell) {
        this.environment = cell.environment;
        this.state = cell.state;
        this.counted = cell.counted;
        this.alive = cell.alive;
    }

    /**
     * @return The cell's state.
     */
    public boolean getState() {
        return state;
    }

    /**
     * If it was not already calculated, counts the amount of cells that are alive around this cell
     * and returns it.
     * @return The amount of alive cells around of this one.
     */
    public int getAliveCount() {
        if(!counted) {
            countAliveNeighbors();
            counted = true;
        }

        return alive;
    }

    /**
     * Replaces the cell's state with a random one.
     */
    public void randomState() {
        state = random.nextBoolean();
        counted = false;
    }

    /**
     * Counts the amount of cells that are alive around this one.
     */
    private void countAliveNeighbors() {
        alive = 0;

        alive += environment.getCell(this, Direction.upL).state ? 1 : 0;
        alive += environment.getCell(this, Direction.upR).state ? 1 : 0;
        alive += environment.getCell(this, Direction.left).state ? 1 : 0;
        alive += environment.getCell(this, Direction.right).state ? 1 : 0;
        alive += environment.getCell(this, Direction.downL).state ? 1 : 0;
        alive += environment.getCell(this, Direction.downR).state ? 1 : 0;
    }

    /**
     * Applies the rules of the Game of Life to determine the new state of the cell.
     */
    public void nextState() {
        if(!counted) {
            countAliveNeighbors();
        }

        if(state) {
            /* Survival */
            state = alive == 2 || alive == 3;
        } else {
            /* Birth */
            state = alive == 3;
        }

        counted = false;
    }
}
