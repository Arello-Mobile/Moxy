package com.arellomobile.mvp;

import android.os.Bundle;

import com.arellomobile.mvp.presenter.PresenterType;

import java.util.ArrayList;
import java.util.List;

/**
 * Date: 18-Dec-15
 * Time: 13:51
 * <p>
 * This class represents a delegate which you can use to extend Mvp's support to any class.
 * <p>
 * When using an {@link MvpDelegate}, lifecycle methods which should be proxied to the delegate:
 * <ul>
 * <li>{@link #onCreate(Bundle)}</li>
 * <li>{@link #onAttach()}: inside onStart() of Activity or Fragment</li>
 * <li>{@link #onSaveInstanceState(android.os.Bundle)}</li>
 * <li>{@link #onDetach()}: inside onDestroy() for Activity or onDestroyView() for Fragment</li>
 * <li>{@link #onDestroy()}</li>
 * </ul>
 * <p>
 * Every {@link Object} can only be linked with one {@link MvpDelegate} instance,
 * so the instance returned from {@link #MvpDelegate(Object)}} should be kept
 * until the Object is destroyed.
 *
 * @author Yuri Shmakov
 * @author Alexander Blinov
 * @author Konstantin Tckhovrebov
 */
public class MvpDelegate<Delegated> {
	private static final String KEY_TAG = "com.arellomobile.mvp.MvpDelegate.KEY_TAG";
	private final Delegated mDelegated;
	private String mKeyTag = KEY_TAG;
	private String mDelegateTag;
	private boolean mIsAttached;
	private MvpDelegate mParentDelegate;
	private List<MvpPresenter<? super Delegated>> mPresenters;
	private List<MvpDelegate> mChildDelegates;
	private Bundle mBundle;
	private Bundle mChildKeyTagsBundle;
	private boolean mDestroyViewOnDetach = false;

	public MvpDelegate(Delegated delegated) {
		mDelegated = delegated;
		mChildDelegates = new ArrayList<>();
		mChildKeyTagsBundle = new Bundle();
	}

	public void setParentDelegate(MvpDelegate delegate, String childId) {
		if (mBundle != null) {
			throw new IllegalStateException("You should call setParentDelegate() before first onCreate()");
		}
		if (mChildDelegates != null && mChildDelegates.size() > 0) {
			throw new IllegalStateException("You could not set parent delegate when it already has child presenters");
		}

		mParentDelegate = delegate;
		mKeyTag = mParentDelegate.mKeyTag + "$" + childId;

		delegate.addChildDelegate(this);
	}

	public void setDestroyViewOnDetach(final boolean destroyViewOnDetach) {
		this.mDestroyViewOnDetach = destroyViewOnDetach;
	}

	private void addChildDelegate(MvpDelegate delegate) {
		mChildDelegates.add(delegate);
	}

	/**
	 * <p>Similar like {@link #onCreate(Bundle)}. But this method try to get saved
	 * state from parent presenter before get presenters</p>
	 */
	public void onCreate() {
		Bundle bundle = new Bundle();
		if (mParentDelegate != null) {
			bundle = mParentDelegate.mBundle;
		}

		onCreate(bundle);
	}

	/**
	 * <p>Get(or create if not exists) presenters for delegated object and bind
	 * them to this object fields</p>
	 *
	 * @param bundle with saved state
	 */
	public void onCreate(Bundle bundle) {
		mIsAttached = false;
		mBundle = bundle != null ? bundle : new Bundle();

		//get base tag for presenters
		if (bundle == null || !mBundle.containsKey(mKeyTag)) {
			mDelegateTag = generateTag();
		} else {
			mDelegateTag = bundle.getString(mKeyTag);
		}

		//bind presenters to view
		mPresenters = MvpFacade.getInstance().getMvpProcessor().getMvpPresenters(mDelegated, mDelegateTag);

		for (MvpDelegate childDelegate : mChildDelegates) {
			childDelegate.onCreate(bundle);
		}
	}

	/**
	 * <p>Attach delegated object as view to presenter fields of this object.
	 * If delegate did not enter at {@link #onCreate(Bundle)}(or
	 * {@link #onCreate()}) before this method, then view will not be attached to
	 * presenters</p>
	 */
	public void onAttach() {

		for (MvpPresenter<? super Delegated> presenter : mPresenters) {
			if (mIsAttached && presenter.getAttachedViews().contains(mDelegated)) {
				continue;
			}

			presenter.attachView(mDelegated);
		}

		for (MvpDelegate<?> childDelegate : mChildDelegates) {
			childDelegate.onAttach();
		}

		mIsAttached = true;
	}

	/**
	 * <p>Detach delegated object from their presenters.</p>
	 */
	public void onDetach() {
		for (MvpPresenter<? super Delegated> presenter : mPresenters) {
			if (!mIsAttached && !presenter.getAttachedViews().contains(mDelegated)) {
				continue;
			}

			presenter.detachView(mDelegated);
			if (mDestroyViewOnDetach) {
				presenter.destroyView(mDelegated);
			}
		}

		mIsAttached = false;

		for (MvpDelegate<?> childDelegate : mChildDelegates) {
			childDelegate.onDetach();
			if (mDestroyViewOnDetach) {
				childDelegate.onDestroyView();
			}
		}
	}

	/**
	 * <p>View was being destroyed, but logical unit still alive</p>
	 */
	public void onDestroyView() {
		for (MvpPresenter<? super Delegated> presenter : mPresenters) {
			presenter.destroyView(mDelegated);
		}

		for (MvpDelegate<?> childDelegate : mChildDelegates) {
			childDelegate.onDestroyView();
		}
	}

	/**
	 * <p>Destroy presenters.</p>
	 */
	public void onDestroy() {
		PresentersCounter presentersCounter = MvpFacade.getInstance().getPresentersCounter();
		PresenterStore presenterStore = MvpFacade.getInstance().getPresenterStore();

		for (MvpPresenter<?> presenter : mPresenters) {
			boolean isRejected = presentersCounter.rejectPresenter(presenter, mDelegateTag);
			if (isRejected && presenter.getPresenterType() != PresenterType.GLOBAL) {
				presenterStore.remove(presenter.getPresenterType(), presenter.getTag(), presenter.getPresenterClass());
				presenter.onDestroy();
			}
		}

		for (MvpDelegate<?> childDelegate : mChildDelegates) {
			childDelegate.onDestroy();
		}
	}

	/**
	 * <p>Similar like {@link #onSaveInstanceState(Bundle)}. But this method try to save
	 * state to parent presenter Bundle</p>
	 */
	public void onSaveInstanceState() {
		Bundle bundle = new Bundle();
		if (mParentDelegate != null) {
			bundle = mParentDelegate.mChildKeyTagsBundle;
		}

		onSaveInstanceState(bundle);

		mParentDelegate.mBundle.putAll(bundle);
	}

	/**
	 * Save presenters tag prefix to save state for restore presenters at future after delegate recreate
	 *
	 * @param outState out state from Android component
	 */
	public void onSaveInstanceState(Bundle outState) {
		outState.putAll(mChildKeyTagsBundle);
		outState.putString(mKeyTag, mDelegateTag);

		for (MvpDelegate childDelegate : mChildDelegates) {
			childDelegate.onSaveInstanceState(outState);
		}
	}

	public Bundle getChildrenSaveState() {
		return mBundle;
	}

	/**
	 * @return generated tag in format: Delegated_class_full_name$MvpDelegate@hashCode
	 * <p>
	 * example: com.arellomobile.com.arellomobile.mvp.sample.SampleFragment$MvpDelegate@32649b0
	 */
	private String generateTag() {
		return mDelegated.getClass().getName() + "$" + getClass().getSimpleName() + toString().replace(getClass().getName(), "");
	}
}
