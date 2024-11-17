package model;

import java.util.Random;

public class Cell {
    private static final Random random = new Random();
    private final Environment environment;
    private boolean state;
    private boolean counted;
    private int alive;

    public Cell(Environment environment) {
        this.environment = environment;
        randomState();
        this.counted = false;
        this.alive = 0;
    }

    public Cell(Cell cell) {
        this.environment = cell.environment;
        this.state = cell.state;
        this.counted = cell.counted;
        this.alive = cell.alive;
    }

    public boolean getState() {
        return state;
    }

    public int getAliveCount() {
        if(!counted) {
            countAliveNeighbors();
            counted = true;
        }

        return alive;
    }

    public void randomState() {
        state = random.nextBoolean();
        counted = false;
    }

    public void countAliveNeighbors() {
        alive = 0;

        alive += environment.getCell(this, Direction.upL).state ? 1 : 0;
        alive += environment.getCell(this, Direction.upR).state ? 1 : 0;
        alive += environment.getCell(this, Direction.left).state ? 1 : 0;
        alive += environment.getCell(this, Direction.right).state ? 1 : 0;
        alive += environment.getCell(this, Direction.downL).state ? 1 : 0;
        alive += environment.getCell(this, Direction.downR).state ? 1 : 0;
    }

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
