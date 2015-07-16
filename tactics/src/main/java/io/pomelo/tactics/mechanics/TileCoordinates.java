package io.pomelo.tactics.mechanics;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Custom class used for Tile based elements
 *
 * @author Aekasitt
 */
public class TileCoordinates implements Parcelable {
    /**
     * Parcel CREATOR
     */
    public Parcelable.Creator<TileCoordinates> CREATOR = new Parcelable.Creator<TileCoordinates>() {
        @Override
        public TileCoordinates createFromParcel(Parcel source) {
            return new TileCoordinates(source);
        }

        @Override
        public TileCoordinates[] newArray(int size) {
            return new TileCoordinates[size];
        }
    };
    int x, y;

    /**
     * Default Constructor
     */
    public TileCoordinates() {
        super();
    }

    /**
     * Parcel Constructor
     *
     * @param parcel
     */
    public TileCoordinates(Parcel parcel) {
        x = parcel.readInt();
        y = parcel.readInt();
    }

    /**
     * Set
     *
     * @param x
     * @param y
     * @return this TileCoordinates
     */
    public TileCoordinates setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public float getCenterX() {
        return ((this.x * Element.TILE_SIZE) + (Element.TILE_SIZE / 2));
    }

    public float getCenterY() {
        return ((this.y * Element.TILE_SIZE) + (Element.TILE_SIZE / 2));
    }

    @Override
    public boolean equals(Object other) {
        TileCoordinates target = (other.getClass() == TileCoordinates.class) ? (TileCoordinates) other : null;
        return (target != null && this.x == target.x && this.y == target.y);
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
     * @param flags  <not used>
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(x);
        parcel.writeInt(y);
    }
}
