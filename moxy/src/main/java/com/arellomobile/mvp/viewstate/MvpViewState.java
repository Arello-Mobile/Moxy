package com.arellomobile.mvp.viewstate;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;

import com.arellomobile.mvp.MvpView;

/**
 * Date: 15.12.2015
 * Time: 19:58
 *
 * @author Yuri Shmakov
 */
public abstract class MvpViewState<View extends MvpView> {
	protected Set<View> mViews;
	protected Set<View> mInRestoreState;

	public MvpViewState() {
		mViews = Collections.newSetFromMap(new WeakHashMap<View, Boolean>());
		mInRestoreState = Collections.newSetFromMap(new WeakHashMap<View, Boolean>());
	}

	/**
	 * Apply saved state to attached view
	 *
	 * @param view mvp view to restore state
	 */
	protected abstract void restoreState(View view);

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

		restoreState(view);

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
