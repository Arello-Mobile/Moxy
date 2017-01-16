package com.arellomobile.mvp.sample.github.mvp.presenters;

import java.util.Collections;

import com.arellomobile.mvp.sample.github.mvp.models.Repository;
import com.arellomobile.mvp.sample.github.mvp.views.RepositoryView$$State;
import com.arellomobile.mvp.sample.github.test.GithubSampleTestRunner;
import com.arellomobile.mvp.sample.github.test.TestComponentRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@RunWith(GithubSampleTestRunner.class)
public class RepositoryPresenterTest {

	@Rule
	public TestComponentRule testComponentRule = new TestComponentRule();

	@Mock
	RepositoryView$$State repositoryViewState;

	private RepositoryPresenter presenter;
	private Repository repository;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);

		repository = new Repository();

		presenter = new RepositoryPresenter(repository);
		presenter.setViewState(repositoryViewState);
	}

	@Test
	public void repository_shouldShwRepository() {
		presenter.onFirstViewAttach();

		verify(repositoryViewState).showRepository(repository);
		verify(repositoryViewState, never()).updateLike(anyBoolean(), anyBoolean());
	}

	@Test
	public void repository_shouldSetLikeTrueForRepository() {
		presenter.updateLikes(Collections.singletonList(0), Collections.singletonList(0));

		verify(repositoryViewState).updateLike(true, true);
	}

	@Test
	public void repository_shouldSetLikeFalseForRepository() {
		presenter.updateLikes(Collections.singletonList(1), Collections.singletonList(1));

		verify(repositoryViewState).updateLike(false, false);
	}

}