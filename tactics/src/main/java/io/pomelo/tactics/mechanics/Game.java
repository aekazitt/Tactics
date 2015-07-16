package io.pomelo.tactics.mechanics;

import android.graphics.Canvas;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;

public class Game implements Parcelable {
    /* Constants */
    public static final int THREAD_SLEEP_TIME = 32;
    /* Logging Assistants */
    private static final String TAG = Game.class.getSimpleName();
    private static final boolean SHOW_LOG = true;
    /**
     * Parcel CREATOR
     */
    public Parcelable.Creator<Game> CREATOR = new Parcelable.Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel source) {
            return new Game(source);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };
    private Player player;
    private TileCoordinates playerDestination;
    private Level level;
    private FPSMonitor monitor;
    private TileMonitor tileMonitor;
    private Tile selected;
    private int visX, visY, halfVisX, halfVisY;

    /**
     * Default Constructor
     */
    public Game() {
        firstLevel();
        monitor = new FPSMonitor();
        tileMonitor = new TileMonitor();
        player = new Player(3, 3);
    }

    /**
     * Parcel Constructor
     */
    public Game(Parcel parcel) {
        level = parcel.readParcelable(Level.class.getClassLoader());
        monitor = parcel.readParcelable(FPSMonitor.class.getClassLoader());
        tileMonitor = parcel.readParcelable(TileMonitor.class.getClassLoader());
        player = parcel.readParcelable(Player.class.getClassLoader());
    }

    public void drawGame(Canvas canvas) {
        level.draw(canvas);
        monitor.draw(canvas);
        player.draw(canvas);
        tileMonitor.draw(canvas);
    }

    public void updateGame() {
        movePawn(player, playerDestination);
        level.update();
        monitor.update();
        player.update();
    }

    public boolean processInput(MotionEvent event) {
        TileCoordinates touch = new TileCoordinates()
                .setCoordinates((int) (event.getX() / Element.TILE_SIZE),
                        (int) (event.getY() / Element.TILE_SIZE));
        try {
            selected = level.getTile(touch);
            tileMonitor.setCoordinates(selected.getCoordinates(), selected.isWalkable(), player.coordinates);
            if (selected != null && selected.isWalkable())
                playerDestination = selected.getCoordinates();
        } catch (Tile.TileOutOfBoundsException e) {
            if (SHOW_LOG) Log.d(TAG, "Selection is out of specified area.");
            return false;
        }
        return true;
    }
    /************************************************/
    /*				Default Level					*/

    public void movePawn(Pawn pawn, TileCoordinates coor) {
        if (coor == null) return;
        TileCoordinates pawnTile = pawn.getCoordinates();
        /**
         * Conditions
         */
        TileCoordinates rightTile = new TileCoordinates().setCoordinates(pawnTile.x + 1, pawnTile.y);
        TileCoordinates leftTile = new TileCoordinates().setCoordinates(pawnTile.x - 1, pawnTile.y);
        TileCoordinates downTile = new TileCoordinates().setCoordinates(pawnTile.x, pawnTile.y + 1);
        TileCoordinates upTile = new TileCoordinates().setCoordinates(pawnTile.x, pawnTile.y - 1);
        //
        boolean rightWalkable = level.getTile(rightTile).isWalkable();
        boolean leftWalkable = level.getTile(leftTile).isWalkable();
        boolean downWalkable = level.getTile(downTile).isWalkable();
        boolean upWalkable = level.getTile(upTile).isWalkable();
        //
        boolean rightDestined = pawn.getX() < coor.getCenterX();
        boolean leftDestined = pawn.getX() > coor.getCenterX();
        boolean downDestined = pawn.getY() < coor.getCenterY();
        boolean upDestined = pawn.getY() > coor.getCenterY();
        /**
         * Logic
         */
        if (rightWalkable && rightDestined) pawn.move(rightTile);
        else if (leftWalkable && leftDestined) pawn.move(leftTile);
        else if (downWalkable && downDestined) pawn.move(downTile);
        else if (upWalkable && upDestined) pawn.move(upTile);
    }
    /************************************************/
	/*					Parcelable					*/
    /************************************************/

    /**
     * ********************************************
     */
    public void firstLevel() {
        if (SHOW_LOG) Log.d(TAG, "Creating first level");
        visX = visY = 7;
        halfVisX = halfVisY = visX / 2;
        int width = 7;
        int height = 7;
        Tile tiles[] = new Tile[width * height];
        int i = 0;
        for (int j = 0; j < width; j++)
            tiles[(i * width) + j] = new Tile(0, false, j, i);
        //
        for (i = 1; i < height - 1; i++) {
            for (int j = 0; j < width; j++) {
                if ((i == 2 || i == 3) && j == i + 1) {
                    tiles[(i * width) + j] = new Tile(0, false, j, i);
                    continue;
                }
                if (j == 0 || j == 6) tiles[(i * width) + j] = new Tile(0, false, j, i);
                else tiles[(i * width) + j] = new Tile(1, true, j, i);
            }
        }
        for (int j = 0; j < width; j++) tiles[(i * width) + j] = new Tile(0, false, j, height - 1);
        //
        level = new Level(width, height, tiles);
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
        parcel.writeParcelable(level, flags);
        parcel.writeParcelable(monitor, flags);
        parcel.writeParcelable(tileMonitor, flags);
        parcel.writeParcelable(player, flags);
    }
}
