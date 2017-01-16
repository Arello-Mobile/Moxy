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
import com.arellomobile.mvp.sample.github.mvp.presenters.RepositoryWidgetPresenter;
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
	RepositoryWidgetPresenter mRepositoryPresenter;

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
	RepositoryWidgetPresenter provideRepositoryPresenter() {
		return new RepositoryWidgetPresenter();
	}

	public void initWidget(MvpDelegate parentDelegate, Repository repository) {
		mParentDelegate = parentDelegate;
		mRepository = repository;

		getMvpDelegate().onCreate();
		getMvpDelegate().onAttach();

		mRepositoryPresenter.setRepository(mRepository);
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
