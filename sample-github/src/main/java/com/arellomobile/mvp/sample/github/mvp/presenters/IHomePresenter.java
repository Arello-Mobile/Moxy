package com.arellomobile.mvp.sample.github.mvp.presenters;

import com.arellomobile.mvp.IMvpPresenter;
import com.arellomobile.mvp.sample.github.mvp.models.Repository;

public interface IHomePresenter extends ISomePresenter<String>
{
	void onRepositorySelection(int position, Repository repository);
}
