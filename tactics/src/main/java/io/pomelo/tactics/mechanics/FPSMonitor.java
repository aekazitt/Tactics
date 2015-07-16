package io.pomelo.tactics.mechanics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Formatter;

public class FPSMonitor extends Element {
    /* Logging Assistants */
    private static final String TAG = FPSMonitor.class.getSimpleName();
    private static final boolean SHOW_LOG = false;
    /**
     * Constants
     */
    private static final int TEXT_SIZE = 32;
    /**
     * Parcel CREATOR
     */
    public Parcelable.Creator<FPSMonitor> CREATOR = new Parcelable.Creator<FPSMonitor>() {
        @Override
        public FPSMonitor createFromParcel(Parcel source) {
            return new FPSMonitor(source);
        }

        @Override
        public FPSMonitor[] newArray(int size) {
            return new FPSMonitor[size];
        }
    };
    /**
     * Typeface Tools
     */
    private StringBuilder fps;
    private Formatter formatter;
    private Paint paint;
    /**
     * Member Variables
     */
    private int fps_show, fps_check;
    private int lastFrameCheck;

    /**
     * Default Constructor
     */
    public FPSMonitor() {
        init();
        lastFrameCheck = (int) (System.currentTimeMillis());
        fps_check = fps_show = 0;
    }

    /**
     * Parcel Constructor
     */
    public FPSMonitor(Parcel parcel) {
        init();
        fps_show = parcel.readInt();
        fps_check = parcel.readInt();
        lastFrameCheck = parcel.readInt();
    }

    /**
     * Initialize Typeface
     */
    public void init() {
        fps = new StringBuilder();
        formatter = new Formatter(fps);
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTypeface(Typeface.MONOSPACE);
        paint.setTextSize(TEXT_SIZE);
    }

    /**
     * Overrides update: update the count used for FPS checking
     */
    @Override
    protected void update() {
        fps_check++;
        int time = (int) (System.currentTimeMillis());
        if (time - lastFrameCheck >= 1000) {
            if (SHOW_LOG) Log.d(TAG, "update");
            fps_show = fps_check;
            lastFrameCheck = time;
            fps_check = 0;
        }
    }

    @Override
    protected void draw(Canvas canvas) {
        fps.delete(0, fps.length());
        formatter.format("FPS: %d", fps_show);
        if (Element.initiated) {
            canvas.drawText(fps.toString(), Element.bounds.xMax / 2, bounds.yMax - 30, paint);
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
     * @param parcel to write on and saved to Android OS
     * @param flags  <Not-used>
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(fps_show);
        parcel.writeInt(fps_check);
        parcel.writeInt(lastFrameCheck);
    }
}
