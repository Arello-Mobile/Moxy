package com.arellomobile.mvp;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.arellomobile.mvp.presenter.PresenterType;

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
 * @author Alexander Blinov
 * @author Konstantin Tckhovrebov
 */
public class MvpDelegate<Delegated> {
	private static final String KEY_TAGS = "com.arellomobile.mvp.MvpDelegate.KEY_TAGS";

	private String mKeyTags = KEY_TAGS;
	private String mDelegateTag;
	private final Delegated mDelegated;
	private boolean mIsAttached;
	private MvpDelegate mParentDelegate;
	private List<MvpPresenter<? super Delegated>> mPresenters;
	private List<MvpDelegate> mChildDelegates;
	private Bundle mBundle;

	public MvpDelegate(Delegated delegated) {
		mDelegated = delegated;
		mChildDelegates = new ArrayList<>();
	}

	public void setParentDelegate(MvpDelegate delegate, String childId) {
		if (mBundle != null) {
			throw new IllegalStateException("You should call setParentDelegate() before first onCreate()");
		}
		if (mChildDelegates != null && mChildDelegates.size() > 0) {
			throw new IllegalStateException("You could not set parent delegate when it already has child presenters");
		}

		mParentDelegate = delegate;
		mKeyTags = mParentDelegate.mKeyTags + "$" + childId;

		delegate.addChildDelegate(this);
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
		if (bundle == null || !mBundle.containsKey(mKeyTags)) {
			mDelegateTag = generateTag();
		} else {
			mDelegateTag = bundle.getString(mKeyTags);
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
			presenter.detachView(mDelegated);
		}

		mIsAttached = false;

		for (MvpDelegate<?> childDelegate : mChildDelegates) {
			childDelegate.onDetach();
		}
	}

	/**
	 * <p>Destroy presenters.</p>
	 */
	public void onDestroy() {
		PresenterStore presenterStore = MvpFacade.getInstance().getPresenterStore();

		for (MvpPresenter<?> presenter : mPresenters) {
			if (presenter.getPresenterType() == PresenterType.LOCAL) {
				presenter.onDestroy();
				presenterStore.remove(PresenterType.LOCAL, presenter.getTag(), presenter.getPresenterClass());
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
			bundle = mParentDelegate.mBundle;
		} else {

		}

		onSaveInstanceState(bundle);
	}

	/**
	 * Save presenters tag prefix to save state for restore presenters at future after delegate recreate
	 *
	 * @param outState out state from Android component
	 */
	public void onSaveInstanceState(Bundle outState) {
		outState.putAll(mBundle);
		outState.putString(mKeyTags, mDelegateTag);

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
