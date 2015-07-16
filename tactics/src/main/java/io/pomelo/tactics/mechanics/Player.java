package io.pomelo.tactics.mechanics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import io.pomelo.tactics.R;

public class Player extends io.pomelo.tactics.mechanics.Pawn {
    private static final String TAG = Player.class.getSimpleName();
    private static final boolean SHOW_LOG = false;

    public Player(int x, int y) {
        super();
        if (SHOW_LOG) Log.d(TAG, "constructor");
        /**
         * Initialize member variables
         */
        this.x = (int) ((TILE_SIZE * x) + (TILE_SIZE / 2));
        this.y = (int) ((TILE_SIZE * y) + (TILE_SIZE / 2));
        this.width = 120;
        this.height = 120;
        this.draw_x = this.x - this.width / 2;
        this.draw_y = this.y - this.height / 2;
        this.movementSpeed = 30;
        this.coordinates.setCoordinates(x, y);
        this.currentBitmap = BitmapFactory.decodeResource(res, R.drawable.player_base);
    }
        /**
     * Does nothing yet
     */
    @Override
    protected void updateActionableArea() {
    }

    /**
     * Does nothing yet
     */
    @Override
    protected void cycleImage() {
        switch (state) {
            case IDLE:
                break;
            case MOVING:
                break;
            default:
        }
    }

    /**
     * Does nothing yet
     */
    @Override
    public void action(Pawn target) {

    }

    /*********************************/
    /**         Parcelable          **/
    /*********************************/
    /**
     * Parcel Constructor
     */
    public Player(Parcel parcel) {
        this.x = parcel.readFloat();
        this.y = parcel.readFloat();
        this.width = parcel.readInt();
        this.height = parcel.readInt();
        this.draw_x = parcel.readFloat();
        this.draw_y = parcel.readFloat();
        this.movementSpeed = parcel.readFloat();
        this.coordinates = parcel.readParcelable(TileCoordinates.class.getClassLoader());
        this.currentBitmap = parcel.readParcelable(Bitmap.class.getClassLoader());
        //
        float[] positionValues = new float[9];
        parcel.readFloatArray(positionValues);
        this.position.setValues(positionValues);
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
     * @param parcel to write on and saved by the Android OS
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeFloat(x);
        parcel.writeFloat(y);
        parcel.writeInt(width);
        parcel.writeInt(height);
        parcel.writeFloat(draw_x);
        parcel.writeFloat(draw_y);
        parcel.writeFloat(movementSpeed);
        parcel.writeParcelable(coordinates, flags);
        parcel.writeParcelable(currentBitmap, flags);
        //
        float[] values = new float[9];
        position.getValues(values);
        parcel.writeFloatArray(values);
    }
    /**
     * Parcel CREATOR
     */
    @SuppressWarnings("unused")
    public Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel source) {
            return new Player(source);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };
}
