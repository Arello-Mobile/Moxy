package com.arellomobile.mvp.sample.github.mvp.presenters;

import com.arellomobile.mvp.sample.github.BuildConfig;
import com.arellomobile.mvp.sample.github.mvp.common.AuthUtils;
import com.arellomobile.mvp.sample.github.mvp.views.SplashView;
import com.arellomobile.mvp.sample.github.test.TestComponentRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.verify;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class SplashPresenterTest {

    @Rule
    public TestComponentRule rule = new TestComponentRule();

    @Mock
    SplashView splashView;

    private SplashPresenter splashPresenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        splashPresenter = new SplashPresenter();
        splashPresenter.getAttachedViews().add(splashView);
    }

    @Test
    public void splash_shouldAuthorizedStateFalse() {
        AuthUtils.setToken(null);
        splashPresenter.checkAuthorized();
        verify(splashView).setAuthorized(false);
    }

    @Test
    public void splash_shouldAuthorizedStateTrue() {
        AuthUtils.setToken(null);
        splashPresenter.checkAuthorized();
        verify(splashView).setAuthorized(true);
    }

}
