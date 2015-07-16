package io.pomelo.tactics.views;

import android.annotation.SuppressLint;
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
	/* Logging Assistants */
	private static final String TAG = GameView.class.getSimpleName();
	private static final boolean SHOW_LOG = false;
	/* Member Variables */
	private Bounds bounds;
	private SurfaceHolder holder;
	
	/** enumeration used to control the state of the surfaceView **/ 
	public enum GameState {	CREATED, RUNNING, PAUSED, STOPPED, DESTROYED; }
	private Game game;
	private GameThread gameThread;
	/** gameState is used to control the state of the surfaceView **/
	protected GameState gameState;
	/**
	 * Constructor
	 * @param context
	 */
	public GameView(Context context) {
		super(context);
		init(context);
	}
	/**
	 * Constructor with AttributeSet (When attached to XML Layouts)
	 * @param context
	 * @param attrs
	 */
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	/**
	 * Initiate the Game Element Class
	 */
	@SuppressLint("ClickableViewAccessibility")
	protected void init(Context context) {
		bounds = new Bounds();
		Element.init(context, bounds);
		gameState = GameState.CREATED;
		holder = getHolder();
		holder.addCallback(this);
		setOnTouchListener(this);
		setFocusable(true);
	}
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
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return game.processInput(event);
	}
	/********************************/
	/* 	Surface Callback Functions 	*/
	/********************************/
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
				}
			}
		}
	}
	/******************************/
	/*			Game Thread		  */
	/******************************/
	protected class GameThread extends Thread {
		private SurfaceHolder surfaceHolder;
		private boolean threadIsRunning;
		/**
		 * Constructor used to create GameThread, update is made onto SurfaceHolder from here on.
		 * @param holder
		 */
		public GameThread(SurfaceHolder holder) {
			if (SHOW_LOG) Log.d(TAG, "Thread constructed");
			this.surfaceHolder = holder;
			threadIsRunning = true;
			setName("Tactics' Game Thread");
		}
		/**
		 * Set the boolean value of threadIsRunning
		 * @param run
		 */
		public void setRunning(boolean run) {
			if (SHOW_LOG) Log.d(TAG, "setRunning");
			threadIsRunning = run;
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
