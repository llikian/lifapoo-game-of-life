package model;

/**
 * Contains some helper functions.
 */
public class Utility {

    /**
     * Clamps a value between a minimum value and maximum value.
     *
     * @param min The minimum boundary.
     * @param max The maximum boundary.
     * @param val The value to clamp.
     * @return The clamped value.
     */
    public static double clamp(double min, double max, double val) {
        return Math.min(Math.max(val, min), max);
    }
}
