package model;

public class Utility {
    public static double clamp(double min, double max, double val) {
        return Math.min(Math.max(val, min), max);
    }
}
