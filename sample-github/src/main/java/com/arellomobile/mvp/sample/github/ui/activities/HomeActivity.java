package com.arellomobile.mvp.sample.github.ui.activities;

import java.util.List;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.sample.github.R;
import com.arellomobile.mvp.sample.github.mvp.models.Repository;
import com.arellomobile.mvp.sample.github.mvp.presenters.HomePresenter;
import com.arellomobile.mvp.sample.github.mvp.presenters.RepositoriesPresenter;
import com.arellomobile.mvp.sample.github.mvp.presenters.SignOutPresenter;
import com.arellomobile.mvp.sample.github.mvp.views.HomeView;
import com.arellomobile.mvp.sample.github.mvp.views.RepositoriesView;
import com.arellomobile.mvp.sample.github.mvp.views.SignOutView;
import com.arellomobile.mvp.sample.github.ui.adapters.RepositoriesAdapter;
import com.arellomobile.mvp.sample.github.ui.fragments.DetailsFragment;
import com.arellomobile.mvp.sample.github.ui.views.FrameSwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends MvpAppCompatActivity implements SignOutView, RepositoriesView, HomeView, RepositoriesAdapter.OnScrollToBottomListener {
	@InjectPresenter
	SignOutPresenter mSignOutPresenter;
	@InjectPresenter
	RepositoriesPresenter mRepositoriesPresenter;
	@InjectPresenter
	HomePresenter mHomePresenter;

	@BindView(R.id.activity_home_toolbar)
	Toolbar mToolbar;
	@BindView(R.id.activity_home_swipe_refresh_layout)
	FrameSwipeRefreshLayout mSwipeRefreshLayout;
	@BindView(R.id.activity_home_progress_bar_repositories)
	ProgressBar mRepositoriesProgressBar;
	@BindView(R.id.activity_home_list_view_repositories)
	ListView mRepositoriesListView;
	@BindView(R.id.activity_home_text_view_no_repositories)
	TextView mNoRepositoriesTextView;
	@BindView(R.id.activity_home_frame_layout_details)
	FrameLayout mDetailsFragmeLayout;

	private AlertDialog mErrorDialog;
	private RepositoriesAdapter mRepositoriesAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		ButterKnife.bind(this);

		setSupportActionBar(mToolbar);

		mSwipeRefreshLayout.setListViewChild(mRepositoriesListView);
		mSwipeRefreshLayout.setOnRefreshListener(() -> mRepositoriesPresenter.loadRepositories(true));

		mRepositoriesAdapter = new RepositoriesAdapter(getMvpDelegate(), this);
		mRepositoriesListView.setAdapter(mRepositoriesAdapter);
		mRepositoriesListView.setOnItemClickListener((parent, view, position, id) -> {
			if (mRepositoriesAdapter.getItemViewType(position) != RepositoriesAdapter.REPOSITORY_VIEW_TYPE) {
				return;
			}

			mHomePresenter.onRepositorySelection(position, mRepositoriesAdapter.getItem(position));
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_home_sign_out) {
			mSignOutPresenter.signOut();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void signOut() {
		final Intent intent = new Intent(this, SplashActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(intent);
	}

	@Override
	public void onStartLoading() {
		mSwipeRefreshLayout.setEnabled(false);
	}

	@Override
	public void onFinishLoading() {
		mSwipeRefreshLayout.setEnabled(true);
	}

	@Override
	public void showRefreshing() {
		mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
	}

	@Override
	public void hideRefreshing() {
		mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));
	}

	@Override
	public void showListProgress() {
		mRepositoriesListView.setVisibility(View.GONE);
		mNoRepositoriesTextView.setVisibility(View.GONE);
		mRepositoriesProgressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideListProgress() {
		mRepositoriesListView.setVisibility(View.VISIBLE);
		mRepositoriesProgressBar.setVisibility(View.GONE);
	}

	@Override
	public void showError(String message) {
		mErrorDialog = new AlertDialog.Builder(this)
				.setTitle(R.string.app_name)
				.setMessage(message)
				.setOnCancelListener(dialog -> mRepositoriesPresenter.onErrorCancel())
				.show();
	}


	@Override
	public void hideError() {
		if (mErrorDialog != null && mErrorDialog.isShowing()) {
			mErrorDialog.hide();
		}
	}

	@Override
	public void setRepositories(List<Repository> repositories, boolean maybeMore) {
		mRepositoriesListView.setEmptyView(mNoRepositoriesTextView);
		mRepositoriesAdapter.setRepositories(repositories, maybeMore);
	}

	@Override
	public void addRepositories(List<Repository> repositories, boolean maybeMore) {
		mRepositoriesListView.setEmptyView(mNoRepositoriesTextView);
		mRepositoriesAdapter.addRepositories(repositories, maybeMore);
	}

	@Override
	public void showDetailsContainer() {
		if (mDetailsFragmeLayout.getVisibility() == View.GONE) {
			mDetailsFragmeLayout.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void setSelection(int position) {
		mRepositoriesAdapter.setSelection(position);
	}

	@Override
	public void showDetails(int position, Repository repository) {
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.activity_home_frame_layout_details, DetailsFragment.getInstance(repository))
				.commit();
	}

	@Override
	public void onScrollToBottom() {
		mRepositoriesPresenter.loadNextRepositories(mRepositoriesAdapter.getRepositoriesCount());
	}

	@Override
	protected void onDestroy() {
		if (mErrorDialog != null && mErrorDialog.isShowing()) {
			mErrorDialog.setOnCancelListener(null);
			mErrorDialog.dismiss();
		}

		super.onDestroy();
	}
}