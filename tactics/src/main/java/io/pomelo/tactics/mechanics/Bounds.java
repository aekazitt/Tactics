package io.pomelo.tactics.mechanics;

public class Bounds {
    public float xMax, xMin, yMin, yMax;

    /**
     * Set the boundary used by all of Game elements to be updated and drawn onto the screen
     *
     * @param x      the x-coordinate of the top left corner of drawable area
     * @param y      the y-coordinate of the top left corner of drawable area
     * @param width  the width of drawable area
     * @param height the height of drawable area
     */
    public Bounds set(int x, int y, int width, int height) {
        this.xMin = x;
        this.yMin = y;
        this.xMax = x + width;
        this.yMax = y + height;
        return this;
    }
}
