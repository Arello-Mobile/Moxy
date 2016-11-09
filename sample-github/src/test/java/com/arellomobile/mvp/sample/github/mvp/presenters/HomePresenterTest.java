package com.arellomobile.mvp.sample.github.mvp.presenters;

import android.content.Context;

import com.arellomobile.mvp.sample.github.app.GithubApp;
import com.arellomobile.mvp.sample.github.di.AppComponent;
import com.arellomobile.mvp.sample.github.mvp.models.Repository;
import com.arellomobile.mvp.sample.github.mvp.views.HomeView;
import com.arellomobile.mvp.sample.github.mvp.views.HomeView$$State;
import com.arellomobile.mvp.sample.github.test.TestComponent;
import com.arellomobile.mvp.sample.github.test.TestComponentRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;

import static org.mockito.Mockito.verify;

public final class HomePresenterTest {

    @Rule
    public TestComponentRule rule = new TestComponentRule();

    @Mock
    HomeView homeView;

    @Mock
    HomeView$$State homeViewState;

    private HomePresenter homePresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        homePresenter = new HomePresenter();
        homePresenter.attachView(homeView);
        homePresenter.setViewState(homeViewState);
    }

    @Test
    public void details_shouldShowDetailsContainer() {
        Repository emptyRepository = emptyRepository();
        homePresenter.onRepositorySelection(0, emptyRepository);
        verify(homeViewState).showDetailsContainer();
        verify(homeViewState).showDetails(0, emptyRepository);
    }

    private Repository emptyRepository() {
        return new Repository();
    }
}