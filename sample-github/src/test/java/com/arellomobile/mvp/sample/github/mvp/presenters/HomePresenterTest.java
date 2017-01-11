package com.arellomobile.mvp.sample.github.mvp.presenters;

import com.arellomobile.mvp.sample.github.mvp.models.Repository;
import com.arellomobile.mvp.sample.github.mvp.views.HomeView;
import com.arellomobile.mvp.sample.github.mvp.views.HomeView$$State;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.mockito.Mockito.verify;

public final class HomePresenterTest {

	@Mock
	HomeView homeView;

	@Mock
	HomeView$$State homeViewState;

	private HomePresenter presenter;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		presenter = new HomePresenter();
		presenter.attachView(homeView);
		presenter.setViewState(homeViewState);
	}

	@Test
	public void details_shouldShowDetailsContainer() {
		Repository emptyRepository = emptyRepository();
		presenter.onRepositorySelection(0, emptyRepository);
		verify(homeViewState).showDetailsContainer();
		verify(homeViewState).showDetails(0, emptyRepository);
	}

	private Repository emptyRepository() {
		return new Repository();
	}
}