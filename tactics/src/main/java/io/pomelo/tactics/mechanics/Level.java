package io.pomelo.tactics.mechanics;

import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class Level extends Element {
    /**
     * Array containing tiles of the level
     */
    private final Tile[] tiles;
    /**
     * Dimensions of the tile-array
     */
    private final int width, height;

    /**
     * Constructor
     *
     * @param width  horizontal dimension of the tile-array
     * @param height vertical dimension of the tile-array
     * @param tiles  tile-array to be cloned and used by the level
     */
    public Level(int width, int height, Tile[] tiles) {
        this.x = this.y = 0;
        this.width = width;
        this.height = height;
        this.tiles = new Tile[width * height];
        System.arraycopy(tiles, 0, this.tiles, 0, width*height);
    }

    /**
     * Create a level using Extensible Markup Language (XML)
     *
     * @param in the InputStream linking to the XML file
     * @return new level
     */
    public static Level create(InputStream in) throws XmlPullParserException, IOException {
        String namespace = null;
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
        } finally {
            in.close();
        }
        return null;
    }




    @Override
    protected void update() {
        if ((width * height) != tiles.length) return;
        for (Tile t : tiles) t.update();
    }

    @Override
    protected void draw(Canvas canvas) {
        if ((width * height) != tiles.length) return;
        for (Tile t : tiles) t.draw(canvas);
    }

    public Tile getTile(TileCoordinates coor) throws Tile.TileOutOfBoundsException {
        int index = (coor.y * width) + coor.x;
        if (index > tiles.length - 1) throw new Tile.TileOutOfBoundsException();
        else return tiles[(coor.y * width) + coor.x];
    }

    /*********************************/
    /**         Parcelable          **/
    /*********************************/
    /**
     * Parcel Constructor
     */
    public Level(Parcel parcel) {
        this.tiles = (Tile[]) parcel.readParcelableArray(Tile.class.getClassLoader());
        this.width = parcel.readInt();
        this.height = parcel.readInt();
    }
    /**
     * @return 0
     */
    @Override
    public int describeContents() {
        return 0;
    }
    /**
     * Parcelable: writeToParcel
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeParcelableArray(this.tiles, flags);
        parcel.writeInt(this.width);
        parcel.writeInt(this.height);
    }
    /**
     * Parcel CREATOR
     */
    public Parcelable.Creator<Level> CREATOR = new Parcelable.Creator<Level>() {
        @Override
        public Level createFromParcel(Parcel source) {
            return new Level(source);
        }

        @Override
        public Level[] newArray(int size) {
            return new Level[size];
        }
    };
}
