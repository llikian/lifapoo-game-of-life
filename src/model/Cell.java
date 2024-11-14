package model;

import java.util.Random;

public class Cell {
    private static final Random random = new Random();
    private final Environment environment;
    private boolean state;
    private int alive;

    public Cell(Environment environment) {
        this.environment = environment;
        this.alive = 0;
        randomState();
    }

    public Cell(Cell cell) {
        this.environment = cell.environment;
        this.state = cell.state;
    }

    public boolean getState() {
        return state;
    }

    public int getAliveCount() {
        return alive;
    }

    public void randomState() {
        state = random.nextBoolean();
    }

    public void nextState() {
        alive = 0;

        alive += environment.getCell(this, Direction.upL).state ? 1 : 0;
        alive += environment.getCell(this, Direction.upR).state ? 1 : 0;
        alive += environment.getCell(this, Direction.left).state ? 1 : 0;
        alive += environment.getCell(this, Direction.right).state ? 1 : 0;
        alive += environment.getCell(this, Direction.downL).state ? 1 : 0;
        alive += environment.getCell(this, Direction.downR).state ? 1 : 0;

        if(state) {
            state = alive == 2 || alive == 3; /* Survival */
        } else {
            state = alive == 3; /* Birth */
        }
    }
}
