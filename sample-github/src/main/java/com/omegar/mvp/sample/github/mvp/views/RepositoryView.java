package com.omegar.mvp.sample.github.mvp.views;

import com.omegar.mvp.sample.github.mvp.models.Repository;
import com.omegar.mvp.MvpView;

/**
 * Date: 27.01.2016
 * Time: 21:13
 *
 * @author Yuri Shmakov
 */
public interface RepositoryView extends MvpView {
	void showRepository(Repository repository);

	void updateLike(boolean isInProgress, boolean isLiked);
}
