// Generated code from Butter Knife. Do not modify!
package io.pomelo.tactics.views;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class GameFragment$$ViewBinder<T extends io.pomelo.tactics.views.GameFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492944, "field 'gameView'");
    target.gameView = finder.castView(view, 2131492944, "field 'gameView'");
  }

  @Override public void unbind(T target) {
    target.gameView = null;
  }
}
