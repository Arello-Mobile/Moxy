package com.arellomobile.mvp.sample.github.mvp.views;

import com.arellomobile.mvp.sample.github.mvp.models.Repository;
import com.arellomobile.mvp.MvpView;

/**
 * Date: 27.01.2016
 * Time: 20:00
 *
 * @author Yuri Shmakov
 */
public interface HomeView extends MvpView {
	void showDetails(int position, Repository repository);
}
