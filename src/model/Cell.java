package model;

import java.util.Random;

public class Cell {
    public Cell(Environment environment) {
        this.environment = environment;
    }

    public boolean getState() {
        return state;
    }

    public void randomState() {
        state = random.nextBoolean();
    }

    public void nextState() {
        int alive = 0;

        alive += getCell(Direction.upL) ? 1 : 0;
        alive += getCell(Direction.up) ? 1 : 0;
        alive += getCell(Direction.upR) ? 1 : 0;
        alive += getCell(Direction.left) ? 1 : 0;
        alive += getCell(Direction.right) ? 1 : 0;
        alive += getCell(Direction.downL) ? 1 : 0;
        alive += getCell(Direction.down) ? 1 : 0;
        alive += getCell(Direction.downR) ? 1 : 0;

        if(state) {
            state = alive == 2 || alive == 3;
        } else {
            state = alive == 3;
        }
    }

    public boolean getCell(Direction direction) {
        Cell cell = environment.getCell(this, direction);

        return (cell != null) && cell.state;
    }

    private static final Random random = new Random();
    private final Environment environment;
    private boolean state;
}
