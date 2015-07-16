package io.pomelo.tactics.mechanics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Formatter;

public class TileMonitor extends Element {
    /* Logging Assistants */
    private static final String TAG = TileMonitor.class.getSimpleName();
    private static final boolean SHOW_LOG = false;
    /**
     * Constants
     */
    private static final int TEXT_SIZE = 32;
    /**
     * Parcel CREATOR
     */
    public Parcelable.Creator<TileMonitor> CREATOR = new Parcelable.Creator<TileMonitor>() {
        @Override
        public TileMonitor createFromParcel(Parcel source) {
            return new TileMonitor(source);
        }

        @Override
        public TileMonitor[] newArray(int size) {
            return new TileMonitor[size];
        }
    };
    private StringBuilder fps;
    private Formatter formatter;
    private Paint paint;
    /**
     * Member Variables
     */
    private boolean walkable;
    private TileCoordinates playerCoor;

    /**
     * Default Constructor
     */
    public TileMonitor() {
        super();
        if (SHOW_LOG) Log.d(TAG, "default constructor");
        initTypeface();
        coordinates = new TileCoordinates().setCoordinates(999, 999);
        playerCoor = new TileCoordinates().setCoordinates(999, 999);
    }

    /**
     * Parcel Constructor
     */
    public TileMonitor(Parcel parcel) {
        if (SHOW_LOG) Log.d(TAG, "parcel constructor");
        initTypeface();
        coordinates = parcel.readParcelable(TileCoordinates.class.getClassLoader());
        playerCoor = parcel.readParcelable(TileCoordinates.class.getClassLoader());
    }

    /**
     * Initialize Typeface
     */
    public void initTypeface() {
        this.fps = new StringBuilder();
        this.formatter = new Formatter(fps);
        this.paint = new Paint();
        this.paint.setColor(Color.WHITE);
        this.paint.setTypeface(Typeface.MONOSPACE);
        this.paint.setTextSize(TEXT_SIZE);
    }

    /**
     * Update this TileMonitor's coordinates to designated coor
     *
     * @param coor
     */
    protected void setCoordinates(TileCoordinates coor, boolean walkable, TileCoordinates playerCoor) {
        if (SHOW_LOG) Log.d(TAG, "update TileCoordinates");
        this.coordinates = coor;
        this.walkable = walkable;
        this.playerCoor = playerCoor;
    }

    /**
     * Overrides update
     */
    @Override
    protected void update() {
    }

    /**
     * Overrides draw
     */
    @Override
    protected void draw(Canvas canvas) {
        fps.delete(0, fps.length());
        String line1 = "Selected Coordinates: (%d, %d) Walkable: %b";
        String line2 = "Player Coordinates: (%d, %d)";
        if (Element.initiated) {
            float x = bounds.xMin + 50, y = bounds.yMin + 70;
            formatter.format(line1, coordinates.x, coordinates.y, walkable);
            canvas.drawText(fps.toString(), x, y, paint);
            //
            fps.delete(0, fps.length());
            y += paint.ascent() + paint.descent();
            formatter.format(line2, playerCoor.x, playerCoor.y);
            canvas.drawText(fps.toString(), x, y, paint);
        }
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
     * @param flags  used to find the correct parcelable passed on while writing
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelable(coordinates, flags);
        parcel.writeParcelable(playerCoor, flags);
    }
}
