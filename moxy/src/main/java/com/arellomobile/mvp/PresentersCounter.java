package com.arellomobile.mvp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Date: 14-Nov-16
 * Time: 04:39
 *
 * @author Yuri Shmakov
 */
@SuppressWarnings("WeakerAccess")
public class PresentersCounter {

	private Map<MvpPresenter<?>, Set<String>> mConnections = new HashMap<>();
	private Map<String, Set<MvpPresenter>> mTags = new HashMap<>();

	/**
	 * Save delegate tag when it inject presenter to delegate's object
	 *
	 * @param presenter     Injected presenter
	 * @param delegateTag   Delegate tag
	 */
	public void injectPresenter(MvpPresenter<?> presenter, String delegateTag) {
		Set<String> delegateTags = mConnections.get(presenter);
		if (delegateTags == null) {
			delegateTags = new HashSet<>();
			mConnections.put(presenter, delegateTags);
		}

		delegateTags.add(delegateTag);

		Set<MvpPresenter> presenters = mTags.get(delegateTag);
		if (presenters == null) {
			presenters = new HashSet<>();
			mTags.put(delegateTag, presenters);
		}
		presenters.add(presenter);
	}

	/**
	 * Remove tag when delegate's object was fully destroyed
	 *
	 * @param presenter     Rejected presenter
	 * @param delegateTag   Delegate tag
	 * @return              True if there are no links to this presenter and presenter be able to destroy. False otherwise
	 */
	public boolean rejectPresenter(MvpPresenter<?> presenter, String delegateTag) {
		Set<MvpPresenter> presenters = mTags.get(delegateTag);
		if (presenters != null) {
			presenters.remove(presenter);
		}
		if (presenters == null || presenters.isEmpty()) {
			mTags.remove(delegateTag);
		}

		Set<String> delegateTags = mConnections.get(presenter);
		if (delegateTags == null) {
			mConnections.remove(presenter);
			return true;
		}

		Iterator<String> tagsIterator = delegateTags.iterator();
		while (tagsIterator.hasNext()) {
			String tag = tagsIterator.next();

			if (tag.startsWith(delegateTag)) {
				tagsIterator.remove();
			}
		}

		boolean noTags = delegateTags.isEmpty();

		if (noTags) {
			mConnections.remove(presenter);
		}

		return noTags;
	}

	public Set<MvpPresenter> getAll(String delegateTag) {
		Set<MvpPresenter> presenters = new HashSet<>();
		for (Map.Entry<String, Set<MvpPresenter>> tagsWithPresenters : mTags.entrySet()) {
			if (tagsWithPresenters.getKey().startsWith(delegateTag)) {
				presenters.addAll(tagsWithPresenters.getValue());
			}
		}

		return presenters;
	}

	@SuppressWarnings("unused")
	public boolean isInjected(MvpPresenter<?> presenter) {
		Set<String> mDelegateTags = mConnections.get(presenter);

		return mDelegateTags != null && !mDelegateTags.isEmpty();
	}
}
