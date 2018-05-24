package com.arellomobile.mvp;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
 * @author Yuri Shmakov
 * @author Alexander Blinov
 * @author Konstantin Tckhovrebov
 */
public class MvpDelegate<Delegated> {
	private static final String KEY_TAG = "com.arellomobile.mvp.MvpDelegate.KEY_TAG";
	public static final String MOXY_DELEGATE_TAGS_KEY = "MoxyDelegateBundle";

	private String mKeyTag = KEY_TAG;
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
			throw new IllegalStateException("You could not set parent delegate when there are already has child presenters");
		}

		mParentDelegate = delegate;
		mKeyTag = mParentDelegate.mKeyTag + "$" + childId;

		delegate.addChildDelegate(this);
	}

	private void addChildDelegate(MvpDelegate delegate) {
		mChildDelegates.add(delegate);
	}

	private void removeChildDelegate(MvpDelegate delegate) {
		mChildDelegates.remove(delegate);
	}

	/**
	 * Free self link from children list (mChildDelegates) in parent delegate
	 * property mParentDelegate stay keep link to parent delegate for access to
	 * parent bundle for save state in {@link #onSaveInstanceState()}
	 */
	public void freeParentDelegate() {

		if (mParentDelegate == null) {
			throw new IllegalStateException("You should call freeParentDelegate() before first setParentDelegate()");
		}

		mParentDelegate.removeChildDelegate(this);
	}

	public void removeAllChildDelegates()
	{
		// For avoiding ConcurrentModificationException when removing by removeChildDelegate()
		List<MvpDelegate> childDelegatesClone = new ArrayList<MvpDelegate>(mChildDelegates.size());
		childDelegatesClone.addAll(mChildDelegates);

		for (MvpDelegate childDelegate : childDelegatesClone) {
			childDelegate.freeParentDelegate();
		}

		mChildDelegates = new ArrayList<>();
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
		if (mParentDelegate == null && bundle != null) {
			bundle = bundle.getBundle(MOXY_DELEGATE_TAGS_KEY);
		}

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
		}

		mIsAttached = false;

		for (MvpDelegate<?> childDelegate : mChildDelegates) {
			childDelegate.onDetach();
		}
	}

	/**
	 * <p>View was being destroyed, but logical unit still alive</p>
	 */
	public void onDestroyView() {
		for (MvpPresenter<? super Delegated> presenter : mPresenters) {
			presenter.destroyView(mDelegated);
		}

		// For avoiding ConcurrentModificationException when removing from mChildDelegates
		List<MvpDelegate> childDelegatesClone = new ArrayList<MvpDelegate>(mChildDelegates.size());
		childDelegatesClone.addAll(mChildDelegates);

		for (MvpDelegate childDelegate : childDelegatesClone) {
			childDelegate.onDestroyView();
		}

		if (mParentDelegate != null) {
			freeParentDelegate();
		}
	}

	/**
	 * <p>Destroy presenters.</p>
	 */
	public void onDestroy() {
		PresentersCounter presentersCounter = MvpFacade.getInstance().getPresentersCounter();
		PresenterStore presenterStore = MvpFacade.getInstance().getPresenterStore();

		Set<MvpPresenter> allChildPresenters = presentersCounter.getAll(mDelegateTag);
		for (MvpPresenter presenter : allChildPresenters) {
			boolean isRejected = presentersCounter.rejectPresenter(presenter, mDelegateTag);
			if (isRejected && presenter.getPresenterType() != PresenterType.GLOBAL) {
				presenterStore.remove(presenter.getTag());
				presenter.onDestroy();
			}
		}
	}

	/**
	 * <p>Similar like {@link #onSaveInstanceState(Bundle)}. But this method try to save
	 * state to parent presenter Bundle</p>
	 */
	public void onSaveInstanceState() {
		Bundle bundle = new Bundle();
		if (mParentDelegate != null && mParentDelegate.mBundle != null) {
			bundle = mParentDelegate.mBundle;
		}

		onSaveInstanceState(bundle);
	}

	/**
	 * Save presenters tag prefix to save state for restore presenters at future after delegate recreate
	 *
	 * @param outState out state from Android component
	 */
	public void onSaveInstanceState(Bundle outState) {
		if (mParentDelegate == null) {
			Bundle moxyDelegateBundle = new Bundle();
			outState.putBundle(MOXY_DELEGATE_TAGS_KEY, moxyDelegateBundle);
			outState = moxyDelegateBundle;
		}

		outState.putAll(mBundle);
		outState.putString(mKeyTag, mDelegateTag);

		for (MvpDelegate childDelegate : mChildDelegates) {
			childDelegate.onSaveInstanceState(outState);
		}
	}

	public Bundle getChildrenSaveState() {
		return mBundle;
	}

	/**
	 * @return generated tag in format: &lt;parent_delegate_tag&gt; &lt;delegated_class_full_name&gt;$MvpDelegate@&lt;hashCode&gt;
	 * <p>
	 * example: com.arellomobile.mvp.sample.SampleFragment$MvpDelegate@32649b0
	 */
	private String generateTag() {
		String tag = mParentDelegate != null ? mParentDelegate.mDelegateTag  + " " : "";
		tag += mDelegated.getClass().getSimpleName() + "$" + getClass().getSimpleName() + toString().replace(getClass().getName(), "");
		return tag;
	}
}
