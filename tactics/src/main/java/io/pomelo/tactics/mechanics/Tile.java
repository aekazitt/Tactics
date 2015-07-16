package io.pomelo.tactics.mechanics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.pomelo.tactics.R;

public class Tile extends Element {
    /* Logging Assistants */
    private static final String TAG = Tile.class.getSimpleName();
    private static final boolean SHOW_LOG = false;
    private static List<Bitmap> terrains = new ArrayList<Bitmap>();
    /**
     * Parcel CREATOR
     */
    public Parcelable.Creator<Tile> CREATOR = new Parcelable.Creator<Tile>() {
        @Override
        public Tile createFromParcel(Parcel source) {
            return new Tile(source);
        }

        @Override
        public Tile[] newArray(int size) {
            return new Tile[size];
        }
    };
    private int terrainType;
    private boolean walkable;

    /**
     * Constructor
     * @param terrainType integer depicting type of terrain affecting Pawn's movements and actions;
     * @param walkable    boolean depicting whether or not this area is accessible by Pawns;
     * @param x x-coordinate;
     * @param y y-coordinate;
     */
    public Tile(int terrainType, boolean walkable, int x, int y) {
        super();
        this.terrainType = terrainType;
        this.walkable = walkable;
        this.coordinates.setCoordinates(x, y);
    }

    /**
     * Parcel Constructor
     */
    public Tile(Parcel parcel) {
        super();
        this.terrainType = parcel.readInt();
        this.walkable = parcel.readByte() != 0;
        this.coordinates = parcel.readParcelable(TileCoordinates.class.getClassLoader());
        //
        float[] positionValues = new float[9];
        parcel.readFloatArray(positionValues);
        this.position.setValues(positionValues);
    }

    /**
     * Initiate the Tile Types
     */
    public static void init() {
        if (terrains.isEmpty()) {
            terrains.add(BitmapFactory.decodeResource(res, R.drawable.tile_0));
            terrains.add(BitmapFactory.decodeResource(res, R.drawable.tile_1));
            terrains.add(BitmapFactory.decodeResource(res, R.drawable.tile_2));
            terrains.add(BitmapFactory.decodeResource(res, R.drawable.tile_3));
            terrains.add(BitmapFactory.decodeResource(res, R.drawable.tile_4));
        }
    }

    /**
     * @return the terrainType
     */
    public int getTerrainType() {
        return terrainType;
    }

    /**
     * @param terrainType the terrainType to set
     */
    public void setTerrainType(int terrainType) {
        this.terrainType = terrainType;
    }

    /**
     * @return the walkable
     */
    public boolean isWalkable() {
        return walkable;
    }
    /*********************************/
    /**        Element Overrides		**/

    /**
     * @param walkable the walkable to set
     */
    public void setWalkable(boolean walkable) {
        this.walkable = walkable;
    }

    /**
     * *****************************
     */
    @Override
    protected void update() {
        currentBitmap = terrains.get(terrainType);
        if (SHOW_LOG)
            Log.d(TAG, String.format("Bitmap sizes: width=%d height=%d", currentBitmap.getWidth(), currentBitmap.getHeight()));
        if (SHOW_LOG) Log.d(TAG, String.format("Game Default Tile size=%f", TILE_SIZE));
        position.setScale(TILE_SIZE / (float) currentBitmap.getWidth(), TILE_SIZE / (float) currentBitmap.getHeight());
        position.postTranslate(coordinates.x * TILE_SIZE, coordinates.y * TILE_SIZE);
    }

    /**
     * Called to be drawn by the field
     *
     * @param canvas
     */
    @Override
    protected void draw(Canvas canvas) {
        if (currentBitmap == null) return;
        canvas.drawBitmap(currentBitmap, position, null);
    }

    /**
     * Parcelable: describeContents
     *
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Parcelable: writeToParcel
     *
     * @param parcel to write on and saved by Android OS
     * @param flags  to identify designated location this parcel is to be open
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(terrainType);
        parcel.writeByte((byte) (walkable ? 1 : 0));
        parcel.writeParcelable(coordinates, flags);
        float[] values = new float[9];
        position.getValues(values);
        parcel.writeFloatArray(values);
    }

    public static class TileOutOfBoundsException extends RuntimeException {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public TileOutOfBoundsException() {
            super();
        }

        public TileOutOfBoundsException(String message, Throwable cause,
                                        boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause);
        }

        public TileOutOfBoundsException(String message, Throwable cause) {
            super(message, cause);
        }

        public TileOutOfBoundsException(String message) {
            super(message);
        }

        public TileOutOfBoundsException(Throwable cause) {
            super(cause);
        }
    }
}
