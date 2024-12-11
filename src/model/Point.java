package model;

/**
 * A 2D point.
 * <p>
 * Attributes:<br>
 * - x: the x coordinate.<br>
 * - y: the y coordinate.
 */
public class Point {
    public int x;
    public int y;

    /**
     * Creates a point with its coordinates at the origin (0 ; 0).
     */
    public Point() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Creates a point with specified coordinates.
     *
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
