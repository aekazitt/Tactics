/**
 *
 * @author Aekasitt Guruvanich, Hannibal Softworks Development Team Lead
 *
 */
package io.pomelo.tactics.views;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.pomelo.tactics.R;
import io.pomelo.tactics.mechanics.Game;

public class GameFragment extends Fragment {
	/* Logging Assistants */
	private static final String TAG = GameFragment.class.getSimpleName();
	private static final boolean SHOW_LOG = false;
	/**
	 * 
	 */
	private static final String SAVED_INSTANCE_IDENTIFIER = "saved_game_instance";
	/* Variables used for View Creation */
	private Game game;
	/* View and Holder */
	@Bind(R.id.gameView)
	protected GameView gameView;

	/** Default Constructor **/
	public GameFragment() {
		super();
	}
	/**
	 * Constructor used for calling new event
	 * @param context
	 */
	public GameFragment(Context context) {
		super();
	}
	/*******************************/
	/* Fragment/Activity Functions */
	/*******************************/
	/**
	 * Fragment: onCreateView
	 */
	@Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (SHOW_LOG) Log.d(TAG, "onCreateView");
		View view = inflater.inflate(R.layout.fragment_game, container, false);
		ButterKnife.bind(this, view);
		return view;
	}
	/**
	 * Fragment: onActivityCreated
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (SHOW_LOG) Log.d(TAG, "onActivityCreated");
		if (game==null) {
			if (savedInstanceState!=null) 
				game = (Game) savedInstanceState.getParcelable(SAVED_INSTANCE_IDENTIFIER);
			gameView.setGame((game!=null)? game : new Game());
		}
		game = gameView.getGame();
	}
	@Override
	public void onResume() {
		super.onResume();
		if (SHOW_LOG) Log.d(TAG, "onResume");
	}
	/**
	 * Fragment: onRestoreInstanceState
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (SHOW_LOG) Log.d(TAG, "onSaveInstanceState");
		if (game!=null) outState.putParcelable(SAVED_INSTANCE_IDENTIFIER , game);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		ButterKnife.unbind(this);
	}
}
