package com.arellomobile.mvp.viewstate;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import com.arellomobile.mvp.MvpView;

/**
 * Date: 15.12.2015
 * Time: 19:58
 *
 * @author Yuri Shmakov
 */
@SuppressWarnings("WeakerAccess")
public abstract class MvpViewState<View extends MvpView> {
	protected ViewCommands<View> mViewCommands = new ViewCommands<>();
	protected Set<View> mViews;
	protected Set<View> mInRestoreState;
	protected Map<View, Set<ViewCommand<View>>> mViewStates;

	public MvpViewState() {
		mViews = Collections.newSetFromMap(new WeakHashMap<View, Boolean>());
		mInRestoreState = Collections.newSetFromMap(new WeakHashMap<View, Boolean>());
		mViewStates = new WeakHashMap<>();
	}

	/**
	 * Apply saved state to attached view
	 *
	 * @param view mvp view to restore state
	 * @param currentState commands that was applied already
	 */
	protected void restoreState(View view, Set<ViewCommand<View>> currentState) {
		if (mViewCommands.isEmpty()) {
			return;
		}

		mViewCommands.reapply(view, currentState);
	}

	/**
	 * Attach view to view state and apply saves state
	 *
	 * @param view attachment
	 */
	public void attachView(View view) {
		if (view == null) {
			throw new IllegalArgumentException("Mvp view must be not null");
		}

		boolean isViewAdded = mViews.add(view);

		if (!isViewAdded) {
			return;
		}

		mInRestoreState.add(view);

		Set<ViewCommand<View>> currentState = mViewStates.get(view);
		currentState = currentState == null ? Collections.<ViewCommand<View>>emptySet() : currentState;

		restoreState(view, currentState);

		mViewStates.remove(view);

		mInRestoreState.remove(view);
	}

	/**
	 * <p>Detach view from view state. After this moment view state save
	 * commands via
	 * {@link com.arellomobile.mvp.viewstate.strategy.StateStrategy#beforeApply(List, ViewCommand)}.</p>
	 *
	 * @param view target mvp view to detach
	 */
	public void detachView(View view) {
		mViews.remove(view);
		mInRestoreState.remove(view);

		Set<ViewCommand<View>> currentState = Collections.newSetFromMap(new WeakHashMap<ViewCommand<View>, Boolean>());
		currentState.addAll(mViewCommands.getCurrentState());
		mViewStates.put(view, currentState);
	}

	public void destroyView(View view) {
		mViewStates.remove(view);
	}

	/**
	 * @return views, attached to this view state instance
	 */
	public Set<View> getViews() {
		return mViews;
	}

	/**
	 * Check if view is in restore state or not
	 *
	 * @param view view for check
	 * @return true if view state restore state to incoming view. false otherwise.
	 */
	public boolean isInRestoreState(View view) {
		return mInRestoreState.contains(view);
	}
}
