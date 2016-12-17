package com.arellomobile.mvp.sample.github.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.Checkable;

/**
 * Date: 26.01.2016
 * Time: 17:19
 *
 * @author Yuri Shmakov
 */
public class LikeButton extends Button implements Checkable {
	private boolean mChecked;

	public LikeButton(Context context) {
		super(context);
	}

	public LikeButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public LikeButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public LikeButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	public void setChecked(boolean checked) {
		mChecked = checked;
	}

	@Override
	public boolean isChecked() {
		return mChecked;
	}

	@Override
	public void toggle() {
		mChecked = !mChecked;
	}
}
