package com.arellomobile.mvp.sample.github.ui.views;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

import com.arellomobile.mvp.MvpDelegate;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.arellomobile.mvp.sample.github.mvp.models.Repository;
import com.arellomobile.mvp.sample.github.mvp.presenters.RepositoryPresenter;
import com.arellomobile.mvp.sample.github.mvp.views.RepositoryView;

/**
 * Created by senneco on 23.10.2016
 */
/**
 * Date: 23.10.2016
 * Time: 10:19
 *
 * @author Yuri Shmakov
 */
public class RepositoryWidget extends TextView implements RepositoryView {
	private MvpDelegate mParentDelegate;
	private MvpDelegate mMvpDelegate;
	private Repository mRepository;

	@InjectPresenter
	RepositoryPresenter mRepositoryPresenter;

	public RepositoryWidget(Context context) {
		super(context);
	}

	public RepositoryWidget(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RepositoryWidget(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public RepositoryWidget(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@ProvidePresenter
	RepositoryPresenter provideRepositoryPresenter() {
		return new RepositoryPresenter(mRepository);
	}

	public void initWidget(MvpDelegate parentDelegate, Repository repository) {
		mParentDelegate = parentDelegate;
		mRepository = repository;

		getMvpDelegate().onCreate();
		getMvpDelegate().onAttach();
	}

	public MvpDelegate getMvpDelegate() {
		if (mMvpDelegate == null) {
			mMvpDelegate = new MvpDelegate<>(this);
			mMvpDelegate.setParentDelegate(mParentDelegate, String.valueOf(mRepository.getId()));
		}
		return mMvpDelegate;
	}

	@Override
	public void showRepository(Repository repository) {
		setText(repository.getName());
	}

	@Override
	public void updateLike(boolean isInProgress, boolean isLiked) {
		// pass
	}
}
