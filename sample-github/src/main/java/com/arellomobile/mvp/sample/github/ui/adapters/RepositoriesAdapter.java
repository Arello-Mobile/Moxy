package com.arellomobile.mvp.sample.github.ui.adapters;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.arellomobile.mvp.sample.github.R;
import com.arellomobile.mvp.sample.github.mvp.models.Repository;
import com.arellomobile.mvp.sample.github.mvp.presenters.RepositoryLikesPresenter;
import com.arellomobile.mvp.sample.github.mvp.presenters.RepositoryPresenter;
import com.arellomobile.mvp.sample.github.mvp.views.RepositoryLikesView;
import com.arellomobile.mvp.sample.github.mvp.views.RepositoryView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 22.01.2016
 * Time: 17:04
 *
 * @author Yuri Shmakov
 */
public class RepositoriesAdapter extends MvpBaseAdapter implements RepositoryLikesView {
	public static final int REPOSITORY_VIEW_TYPE = 0;
	private static final int PROGRESS_VIEW_TYPE = 1;

	@InjectPresenter(type = PresenterType.WEAK, tag = RepositoryLikesPresenter.TAG)
	RepositoryLikesPresenter mRepositoryLikesPresenter;

	private int mSelection = -1;
	private List<Repository> mRepositories;
	private List<Integer> mLiked;
	private List<Integer> mLikesInProgress;
	private boolean mMaybeMore;
	private OnScrollToBottomListener mScrollToBottomListener;

	public RepositoriesAdapter(MvpDelegate<?> parentDelegate, OnScrollToBottomListener scrollToBottomListener) {
		super(parentDelegate, String.valueOf(0));

		mScrollToBottomListener = scrollToBottomListener;
		mRepositories = new ArrayList<>();
		mLiked = new ArrayList<>();
		mLikesInProgress = new ArrayList<>();
	}

	public void setRepositories(List<Repository> repositories, boolean maybeMore) {
		mRepositories = new ArrayList<>(repositories);
		dataSetChanged(maybeMore);
	}

	public void addRepositories(List<Repository> repositories, boolean maybeMore) {
		mRepositories.addAll(repositories);
		dataSetChanged(maybeMore);
	}

	public void updateLikes(List<Integer> inProgress, List<Integer> likedIds) {
		mLikesInProgress = new ArrayList<>(inProgress);
		mLiked = new ArrayList<>(likedIds);

		notifyDataSetChanged();
	}

	public void setSelection(int selection) {
		mSelection = selection;

		notifyDataSetChanged();
	}

	private void dataSetChanged(boolean maybeMore) {
		mMaybeMore = maybeMore;

		notifyDataSetChanged();
	}

	@Override
	public int getItemViewType(int position) {
		return position == mRepositories.size() ? PROGRESS_VIEW_TYPE : REPOSITORY_VIEW_TYPE;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	public int getRepositoriesCount() {
		return mRepositories.size();
	}

	@Override
	public int getCount() {
		return mRepositories.size() + (mMaybeMore ? 1 : 0);
	}

	@Override
	public Repository getItem(int position) {
		return mRepositories.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (getItemViewType(position) == PROGRESS_VIEW_TYPE) {
			if (mScrollToBottomListener != null) {
				mScrollToBottomListener.onScrollToBottom();
			}

			return new ProgressBar(parent.getContext());
		}

		RepositoryHolder holder;
		if (convertView != null) {
			holder = (RepositoryHolder) convertView.getTag();
		} else {
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repository, parent, false);
			holder = new RepositoryHolder(convertView);
			convertView.setTag(holder);
		}

		final Repository item = getItem(position);

		holder.bind(position, item);

		return convertView;
	}

	public class RepositoryHolder implements RepositoryView {

		@InjectPresenter
		RepositoryPresenter mRepositoryPresenter;

		private Repository mRepository;

		@BindView(R.id.item_repository_text_view_name)
		TextView nameTextView;
		@BindView(R.id.item_repository_image_button_like)
		ImageButton likeImageButton;
		View view;

		private MvpDelegate mMvpDelegate;

		@ProvidePresenter
		RepositoryPresenter provideRepositoryPresenter() {
			return new RepositoryPresenter(mRepository);
		}

		RepositoryHolder(View view) {
			this.view = view;

			ButterKnife.bind(this, view);
		}

		void bind(int position, Repository repository) {
			if (getMvpDelegate() != null) {
				getMvpDelegate().onSaveInstanceState();
				getMvpDelegate().onDetach();
				getMvpDelegate().onDestroyView();
				mMvpDelegate = null;
			}

			mRepository = repository;

			getMvpDelegate().onCreate();
			getMvpDelegate().onAttach();

			view.setBackgroundResource(position == mSelection ? R.color.colorAccent : android.R.color.transparent);

			likeImageButton.setOnClickListener(v -> mRepositoryLikesPresenter.toggleLike(repository.getId()));

			boolean isInProgress = mLikesInProgress.contains(repository.getId());

			likeImageButton.setEnabled(!isInProgress);
			likeImageButton.setSelected(mLiked.contains(repository.getId()));
		}

		@Override
		public void showRepository(Repository repository) {
			nameTextView.setText(repository.getName());
		}

		@Override
		public void updateLike(boolean isInProgress, boolean isLiked) {
			// pass
		}

		MvpDelegate getMvpDelegate() {
			if (mRepository == null) {
				return null;
			}

			if (mMvpDelegate == null) {
				mMvpDelegate = new MvpDelegate<>(this);
				mMvpDelegate.setParentDelegate(RepositoriesAdapter.this.getMvpDelegate(), String.valueOf(mRepository.getId()));

			}
			return mMvpDelegate;
		}
	}

	public interface OnScrollToBottomListener {
		void onScrollToBottom();
	}
}
