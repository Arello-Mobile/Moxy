package com.arellomobile.mvp.sample.github.ui.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;

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
public class SignInActivity extends MvpAppCompatActivity implements SignInView {
	@InjectPresenter
	SignInPresenter mSignInPresenter;

	@BindView(R.id.edEmail)
	EditText edEmail;

	@BindView(R.id.edPassword)
	EditText edPassword;

	@BindView(R.id.bSignIn)
	Button bSignIn;

	@BindView(R.id.login_form)
	View loginFormView;

	@BindView(R.id.progressView)
	View progressView;

	private AlertDialog mErrorDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);

		ButterKnife.bind(this);

		edPassword.setOnEditorActionListener((textView, id, keyEvent) -> {
			if (id == R.id.login || id == EditorInfo.IME_NULL) {
				attemptLogin();
				return true;
			}
			return false;
		});

		bSignIn.setOnClickListener(view -> attemptLogin());
	}

	private void attemptLogin() {
		mSignInPresenter.signIn(edEmail.getText().toString(), edPassword.getText().toString());
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
		progressView.setVisibility(show ? View.VISIBLE : View.GONE);
		loginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
	}

	@Override
	public void failedSignIn(String message) {
		mErrorDialog = new AlertDialog.Builder(this)
				.setTitle(R.string.app_name)
				.setMessage(message)
				.setOnCancelListener(dialog -> mSignInPresenter.onErrorCancel())
				.show();
	}

	@Override
	public void hideError() {
		if (mErrorDialog != null && mErrorDialog.isShowing()) {
			mErrorDialog.cancel();
		}
	}

	@Override
	public void hideFormError() {
		edEmail.setError(null);
		edPassword.setError(null);
	}

	@Override
	public void showFormError(Integer emailError, Integer passwordError) {
		edEmail.setError(emailError == null ? null : getString(emailError));
		edPassword.setError(passwordError == null ? null : getString(passwordError));
	}

	@Override
	public void successSignIn() {
		final Intent intent = new Intent(this, SplashActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

		startActivity(intent);
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

