package io.pomelo.tactics.mechanics;

import android.graphics.Canvas;
import android.util.Log;

public abstract class Pawn extends Element {
    /* Logging Assistants */
    private static final String TAG = Pawn.class.getSimpleName();
    private static final boolean SHOW_LOG = false;
    /* Member Variables */
    protected boolean alive = true;
    protected boolean headRight = false;
    protected float actionable_x, actionable_y, actionable_width, actionable_height;
    protected PawnState state = PawnState.IDLE;

    ;
    // Default Pawn Values:
    protected float movementSpeed = 5;
    /**
     * Default Constructor
     */
    public Pawn() {
        super();
        if (SHOW_LOG) Log.d(TAG, "constructor");
        position.setTranslate(x, y);
    }

    /**
     * Overrides update: update the Position, Actionable Area and the Tile Coordinates assigned to this Pawn
     */
    @Override
    protected void update() {
        if (SHOW_LOG) Log.d(TAG, "update");
        updatePosition();
        updateActionableArea();
    }

    /**
     * Overrides draw: Draw this Pawn onto the canvas given using the Bitmap file given and the position-Matrix.
     */
    @Override
    protected void draw(Canvas canvas) {
        if (SHOW_LOG) Log.d(TAG, "draw");
        cycleImage();
        if (currentBitmap != null && position != null)
            canvas.drawBitmap(currentBitmap, position, null);
    }

    /**
     * @return true when this Pawn is still valid, false when it has been killed
     */
    public boolean isAlive() {
        return alive;
    }

    /**
     * Boundary Check
     *
     * @return true if this Pawn is out of assigned boundary, false otherwise
     */
    public boolean isOutOfBound() {
        if (SHOW_LOG) Log.d(TAG, "isOutOfBound");
        if ((y + height < bounds.yMin) || (x + width < bounds.xMin) || y - width > bounds.yMax || (x - width > bounds.xMax))
            return true;
        return false;
    }

    /**
     * Hit-Test Box Check
     *
     * @return true if this Pawn's actionable area, designated by actionable_x, actionable_y, actionable_width and actionable_height has an overlap with the targeted Pawn's actionable area.
     */
    public boolean hitTest(Pawn target) {
        if (SHOW_LOG) Log.d(TAG, "hitTest");
        /**
         * Conditions
         */
        boolean a = (actionable_x + actionable_width) < target.actionable_x;
        boolean b = (actionable_y + actionable_height) < target.actionable_y;
        boolean c = (target.actionable_x + target.actionable_width) < actionable_x;
        boolean d = (target.actionable_y + target.actionable_height) < actionable_y;
        /**
         * Logic
         */
        if (a || b || c || d) return false;
        return true;
    }

    /**
     * Kill this Pawn
     */
    public void die() {
        alive = false;
    }

    /**
     * Change the state of this Pawn and set the destination
     * @param destination the coordinates of the destination
     */
    public void move(TileCoordinates destination) {
        if (!destination.equals(coordinates)) {
            float distanceX = (destination.x * TILE_SIZE) + TILE_SIZE / 2 - x;
            float distanceY = (destination.y * TILE_SIZE) + TILE_SIZE / 2 - y;
            if (distanceX == 0 && distanceY == 0) {
                state = PawnState.IDLE;
                updateTileCoordinates();
                return;
            }
            state = PawnState.MOVING;
            int dirX = destination.x - coordinates.x;
            int dirY = destination.y - coordinates.y;
            x += (Math.abs(distanceX) > movementSpeed) ? dirX * movementSpeed : distanceX;
            y += (Math.abs(distanceY) > movementSpeed) ? dirY * movementSpeed : distanceY;
            distanceX = (destination.x * TILE_SIZE) + TILE_SIZE / 2 - x;
            distanceY = (destination.y * TILE_SIZE) + TILE_SIZE / 2 - y;
            if (distanceX == 0 && distanceY == 0) {
                state = PawnState.IDLE;
                updateTileCoordinates();
            }
        }
    }

    /**
     *
     */
    protected void updatePosition() {
        if (currentBitmap == null) return;
        this.draw_x = x - width / 2;
        this.draw_y = y - height / 2;
        this.position.setScale(width / (float) currentBitmap.getWidth(), height / (float) currentBitmap.getHeight());
        this.position.postTranslate(draw_x, draw_y);
    }

    protected void updateTileCoordinates() {
        if (Element.initiated) coordinates.setCoordinates((int) Math.floor(x / TILE_SIZE),
                (int) Math.floor(y / TILE_SIZE));
    }

    /*
     * Abstract Functions
     */
    abstract protected void updateActionableArea();

    abstract protected void cycleImage();

    abstract public void action(Pawn target);

    public enum PawnState {IDLE, MOVING}
}
