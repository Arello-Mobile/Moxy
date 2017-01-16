package com.arellomobile.mvp.sample.github.ui.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.PresenterType;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.arellomobile.mvp.sample.github.R;
import com.arellomobile.mvp.sample.github.mvp.models.Repository;
import com.arellomobile.mvp.sample.github.mvp.presenters.RepositoryLikesPresenter;
import com.arellomobile.mvp.sample.github.mvp.presenters.RepositoryPresenter;
import com.arellomobile.mvp.sample.github.mvp.views.RepositoryLikesView;
import com.arellomobile.mvp.sample.github.mvp.views.RepositoryView;
import com.arellomobile.mvp.sample.github.ui.views.RepositoryWidget;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Date: 27.01.2016
 * Time: 20:17
 *
 * @author Yuri Shmakov
 */
public class DetailsFragment extends MvpAppCompatFragment implements RepositoryView, RepositoryLikesView {
	public static final String ARGS_REPOSITORY = "argsRepository";

	@InjectPresenter
	RepositoryPresenter mRepositoryPresenter;
	@InjectPresenter(type = PresenterType.WEAK, tag = RepositoryLikesPresenter.TAG)
	RepositoryLikesPresenter mRepositoryLikesPresenter;

	private Repository mRepository;

	@BindView(R.id.fragment_repository_details_text_view_title)
	RepositoryWidget mTitleTextView;
	@BindView(R.id.fragment_repository_details_image_button_like)
	ImageButton mLikeImageButton;

	@ProvidePresenter
	RepositoryPresenter provideRepositoryPresenter() {
		mRepository = (Repository) getArguments().get(ARGS_REPOSITORY);

		return new RepositoryPresenter(mRepository);
	}

	public static DetailsFragment getInstance(Repository repository) {
		DetailsFragment fragment = new DetailsFragment();

		Bundle args = new Bundle();
		args.putSerializable(ARGS_REPOSITORY, repository);

		fragment.setArguments(args);

		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_repository_details, container, false);
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		ButterKnife.bind(this, view);

		mLikeImageButton.setOnClickListener(likeImageButton -> mRepositoryLikesPresenter.toggleLike(mRepository.getId()));
	}

	@Override
	public void updateLikes(List<Integer> inProgress, List<Integer> likedIds) {
		mRepositoryPresenter.updateLikes(inProgress, likedIds);
	}

	@Override
	public void showRepository(Repository repository) {
		mRepository = repository;

		mTitleTextView.initWidget(getMvpDelegate(), repository);
	}

	@Override
	public void updateLike(boolean isInProgress, boolean isLiked) {
		mLikeImageButton.setEnabled(!isInProgress);
		mLikeImageButton.setSelected(isLiked);
	}
}
