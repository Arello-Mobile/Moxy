package com.arellomobile.mvp.sample.github.ui.views;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Date: 26.01.2016
 * Time: 14:54
 *
 * @author Yuri Shmakov
 */
public class FrameSwipeRefreshLayout extends SwipeRefreshLayout {
	private ListView mListViewChild;

	public FrameSwipeRefreshLayout(Context context) {
		super(context);
	}

	public FrameSwipeRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setListViewChild(ListView listViewChild) {
		mListViewChild = listViewChild;
	}

	@Override
	public boolean canChildScrollUp() {
		if (mListViewChild != null && mListViewChild.getVisibility() == VISIBLE) {
			return mListViewChild.canScrollVertically(-1);
		}

		return super.canChildScrollUp();
	}
}
