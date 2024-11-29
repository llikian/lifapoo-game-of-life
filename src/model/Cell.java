package model;

import java.util.Random;

/**
 * A {@link Cell} is a wrapper for a boolean (a state) that contains functionality to simulate
 * Conway's Game of Life.
 * It isn't much use on its own and contains a reference to an {@link Environment} that will allow
 * to link the cells together for the simulation to function.
 * <p>
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
    private boolean nextState;
    private int alive;

    public boolean counted;

    /**
     * Initializes a cell's fields. The initial state of a cell is random.
     *
     * @param environment The environment the cell is a part of.
     */
    public Cell(Environment environment) {
        this.environment = environment;
        randomState();
        this.nextState = false;
        this.counted = false;
        this.alive = 0;
    }

    /**
     * Copy constructor for a cell.
     *
     * @param cell The cell to copy the values from.
     */
    public Cell(Cell cell) {
        this.environment = cell.environment;
        this.state = cell.state;
        this.nextState = cell.nextState;
        this.counted = cell.counted;
        this.alive = cell.alive;
    }

    /**
     * Replaces the state with the new generation's state.
     */
    public void flip() {
        state = nextState;
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
     *
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
     * Changes the cell's state.
     * @param state The cell's new state.
     */
    public void setState(boolean state) {
        this.state = state;
        makeNeighboursCount();
    }

    /**
     * Toggles the cell's state.
     */
    public void toggleState() {
        state = !state;
        makeNeighboursCount();
    }

    /**
     * Sets the cell's state to dead.
     */
    public void kill() {
        state = false;
        makeNeighboursCount();
    }

    /**
     * Replaces the cell's state with a random one.
     */
    public void randomState() {
        state = random.nextBoolean();
    }

    /**
     * Counts the amount of cells that are alive around this one.
     */
    public void countAliveNeighbors() {
        alive = 0;

        for(Direction direction : Direction.values()) {
            alive += environment.getCell(this, direction).state ? 1 : 0;
        }

        counted = true;
    }

    /**
     * Sets the 'counted' field to false in all neighbours, forcing them to recount.
     */
    public void makeNeighboursCount() {
        for(Direction direction : Direction.values()) {
            environment.getCell(this, direction).counted = false;
        }
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
            nextState = alive == 2 || alive == 3;
        } else {
            /* Birth */
            nextState = alive == 3;
        }
    }
}
