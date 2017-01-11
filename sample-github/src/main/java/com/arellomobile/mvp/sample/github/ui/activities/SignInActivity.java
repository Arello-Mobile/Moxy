package com.arellomobile.mvp.sample.github.ui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.sample.github.R;
import com.arellomobile.mvp.sample.github.mvp.presenters.SignInPresenter;
import com.arellomobile.mvp.sample.github.mvp.views.SignInView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A login screen that offers login via email/password.
 */
public class SignInActivity extends MvpAppCompatActivity implements SignInView, DialogInterface.OnCancelListener {
	@InjectPresenter
	SignInPresenter mSignInPresenter;

	@BindView(R.id.email)
	EditText mEmailView;
	@BindView(R.id.password)
	EditText mPasswordView;
	@BindView(R.id.email_sign_in_button)
	Button mSignInButton;
	@BindView(R.id.activity_sign_in_linear_layout_container)
	LinearLayout mContainer;
	@BindView(R.id.login_form)
	View mLoginFormView;
	@BindView(R.id.login_progress)
	View mProgressView;

	private AlertDialog mErrorDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);

		ButterKnife.bind(this);

		mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
			if (id == R.id.login || id == EditorInfo.IME_NULL) {
				attemptLogin();
				return true;
			}
			return false;
		});

		mSignInButton.setOnClickListener(view -> attemptLogin());

		mErrorDialog = new AlertDialog.Builder(this)
				.setTitle(R.string.app_name)
				.setOnCancelListener(this)
				.create();
	}

	private void attemptLogin() {
		mSignInPresenter.signIn(mEmailView.getText().toString(), mPasswordView.getText().toString());
	}

	@Override
	public void startSignIn() {
		toggleProgressVisibility(true);
	}

	@Override
	public void finishSignIn() {
		toggleProgressVisibility(false);
	}

	private void toggleProgressVisibility(final boolean show) {
		mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
		mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
	}

	@Override
	public void failedSignIn(String message) {
		mErrorDialog.setMessage(message);
		mErrorDialog.show();
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		mSignInPresenter.onErrorCancel();
	}

	@Override
	public void hideError() {
		mErrorDialog.cancel();
	}

	@Override
	public void showFormError(Integer emailError, Integer passwordError) {
		if (emailError != null) {
			mEmailView.setError(getString(emailError));
		}
		if (passwordError != null) {
			mPasswordView.setError(getString(passwordError));
		}
	}

	@Override
	public void successSignIn() {
		final Intent intent = new Intent(this, SplashActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		if (mErrorDialog != null) {
			mErrorDialog.setOnCancelListener(null);
			mErrorDialog.dismiss();
		}

		super.onDestroy();
	}
}

