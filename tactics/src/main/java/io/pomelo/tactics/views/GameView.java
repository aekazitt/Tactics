package io.pomelo.tactics.views;

import android.content.Context;
import android.graphics.Canvas;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnTouchListener;

import io.pomelo.tactics.mechanics.Bounds;
import io.pomelo.tactics.mechanics.Element;
import io.pomelo.tactics.mechanics.Game;

public class GameView extends GLSurfaceView implements SurfaceHolder.Callback, OnTouchListener {
	/*********************************/
	/**		Logging Assistant(s) 	**/
	/*********************************/
	private static final String TAG = GameView.class.getSimpleName();
	private static final boolean SHOW_LOG = false;
	/*********************************/
	/**			Constant(s)		 	**/
	/*********************************/
	/**
	 * enumeration used to control the state of the surfaceView
	 */
	public enum GameState {	CREATED, RUNNING, PAUSED, DESTROYED }
	/*********************************/
	/**		Member Variable(s)	 	**/
	/*********************************/
	private Bounds bounds;
	private final SurfaceHolder holder;
	private Game game;
	/**
	 * Runs game thread in the background to update and draw surfaceholder
	 */
	private GameThread gameThread;
	/**
	 * gameState is used to control the state of the surfaceView
	 */
	protected GameState gameState;

	/*********************************/
	/**		  Constructor(s)	 	**/
	/*********************************/
	/**
	 * Constructor
	 * @param context ApplicationContext
	 */
	public GameView(Context context) {
		super(context);
		bounds = new Bounds();
		Element.init(context, bounds);
		gameState = GameState.CREATED;
		holder = getHolder();
		holder.addCallback(this);
		setOnTouchListener(this);
		setFocusable(true);
	}
	/**
	 * Constructor with AttributeSet (When attached to XML Layouts)
	 * @param context ApplicationContext
	 * @param attrs AttributeSets when added to XML layout files
	 */
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		bounds = new Bounds();
		Element.init(context, bounds);
		gameState = GameState.CREATED;
		holder = getHolder();
		holder.addCallback(this);
		setOnTouchListener(this);
		setFocusable(true);
	}
	/*********************************/
	/**	  SurfaceHolder.Callback(s)	**/
	/*********************************/
	/**
	 * Called once when created
	 */
	@Override
	public void onSizeChanged(int w, int h, int oldW, int oldH) {
		if (SHOW_LOG) Log.d(TAG, "SurfaceView onSizeChanged");
		bounds.set(0, 0, w, h);
	}
	/*********************************/
	/**		Member Getters-Setters	**/
	/*********************************/
	/**
	 * @return the game
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * @param game the game to set
	 */
	public void setGame(Game game) {
		this.game = game;
	}
	/*********************************/
	/** 		Touch Listener 		**/
	/*********************************/
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return game.processInput(event);
	}
	/*********************************/
	/**	  SurfaceHolder.Callback(s)	**/
	/*********************************/
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (SHOW_LOG) Log.d(TAG, "surfaceCreated");
		gameThread = new GameThread(holder);
		gameThread.start();
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (SHOW_LOG) Log.d(TAG, "surfaceChanged");
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (SHOW_LOG) Log.d(TAG, "surfaceDestroyed");
		boolean retry = true;
		if (gameThread.isRunning()) {
			gameThread.setRunning(false);
			while(retry) {
				try {
					gameThread.join();
					retry = false;
				} catch (InterruptedException e) {
					if (SHOW_LOG) Log.e(TAG, e.getMessage());
					gameState = GameState.DESTROYED;
				}
			}
		}
	}
	/******************************/
	/*			Game Thread		  */
	/******************************/
	protected class GameThread extends Thread {
		private final SurfaceHolder surfaceHolder;
		private boolean threadIsRunning;
		/**
		 * Constructor used to create GameThread, update is made onto SurfaceHolder from here on.
		 * @param holder the surfaceHolder reference used by GameThread to draw on
		 */
		public GameThread(SurfaceHolder holder) {
			if (SHOW_LOG) Log.d(TAG, "Thread constructed");
			this.surfaceHolder = holder;
			threadIsRunning = true;
			gameState = GameState.RUNNING;
			setName("Tactics' Game Thread");
		}
		/**
		 * Set the boolean value of threadIsRunning
		 * @param run boolean value to set this Thread state
		 */
		public void setRunning(boolean run) {
			if (SHOW_LOG) Log.d(TAG, "setRunning");
			threadIsRunning = run;
			gameState = (run)? GameState.RUNNING : GameState.PAUSED;
		}
		/**
		 * 
		 * @return threadIsRunning
		 */
		public boolean isRunning() {
            if (SHOW_LOG) Log.d(TAG, "isRunning");
			return threadIsRunning;
		}
		@Override
		public void run() {
			if(SHOW_LOG) Log.d(TAG, "run");
			Canvas canvas = null;
			while (threadIsRunning) {
				try {
					int nextRefresh = (int) (System.currentTimeMillis()) + Game.THREAD_SLEEP_TIME;
					canvas = surfaceHolder.lockCanvas(null);
					synchronized(surfaceHolder) {
						if (canvas!=null) {
							game.updateGame();
							game.drawGame(canvas);	
						}
					}
					int interval = nextRefresh - (int) System.currentTimeMillis();
					if (interval < Game.THREAD_SLEEP_TIME && interval > 0) {
						sleep(interval);
					} else if (interval >= Game.THREAD_SLEEP_TIME) {
						sleep(Game.THREAD_SLEEP_TIME);
					}
				} catch (InterruptedException e) {
					if (SHOW_LOG) Log.e(TAG, e.getMessage());
				} finally {
					if (canvas!=null) surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}
}
