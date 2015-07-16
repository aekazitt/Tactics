package io.pomelo.tactics.mechanics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.os.Parcelable;

public abstract class Element implements Parcelable {
    /* Constants */
    public static float TILE_SIZE = 140;
    /* Member Variables */
    protected static Context context;
    protected static Resources res;
    protected static Bounds bounds;
    protected static boolean initiated = false;
    protected Bitmap currentBitmap;
    protected Matrix position;
    protected float x, y, draw_x, draw_y;
    protected TileCoordinates coordinates;
    protected int width, height;

    /**
     * Default constructor
     */
    protected Element() {
        this.position = new Matrix();
        this.coordinates = new TileCoordinates();
    }

    /**
     * Initiate all usage of Elements
     *
     * @param context
     * @param boundary
     */
    public static void init(Context context, Bounds boundary) {
        if (Element.context == null) Element.context = context;
        if (Element.res == null) Element.res = context.getResources();
        if (Element.bounds == null) Element.bounds = boundary;
        Tile.init();
        Element.initiated = true;
    }

    /**
     * @return the currentBitmap
     */
    public Bitmap getCurrentBitmap() {
        return currentBitmap;
    }

    /**
     * @return the x-coordinate of the midpoint of this element-object: Element.x + Element.width/2
     */
    public float getX() {
        return this.x;
    }

    /**
     * @return the y-coordinate of the midpoint of this element-object: Element.y + Element.height/2
     */
    public float getY() {
        return this.y;
    }

    /**
     * @return the coordinates
     */
    public TileCoordinates getCoordinates() {
        return coordinates;
    }

    /**
     * Set the coordinates
     *
     * @param x int-value to set the horizontal coordinate
     * @param y int-value to set the vertical coordinate
     */
    public void setCoordinates(int x, int y) {
        coordinates.setCoordinates(x, y);
    }

    /*
     * abstract functions
     */
    abstract protected void update();

    /**
     * draw on the canvas given with the menu_game element's bitmaps
     *
     * @param canvas the canvas to draw on
     */
    abstract protected void draw(Canvas canvas);
}
