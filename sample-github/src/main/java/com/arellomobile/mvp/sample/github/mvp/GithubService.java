package com.arellomobile.mvp.sample.github.mvp;

import java.util.List;

import com.arellomobile.mvp.sample.github.app.GithubApi;
import com.arellomobile.mvp.sample.github.mvp.models.Repository;
import com.arellomobile.mvp.sample.github.mvp.models.User;

import rx.Observable;

/**
 * Date: 20.09.2016
 * Time: 20:14
 *
 * @author Yuri Shmakov
 */

public class GithubService {

	private GithubApi mGithubApi;

	public GithubService(GithubApi githubApi) {
		mGithubApi = githubApi;
	}


	public Observable<User> signIn(String token) {
		return mGithubApi.signIn(token);
	}

	public Observable<List<Repository>> getUserRepos(String user, int page, Integer pageSize) {
		return mGithubApi.getUserRepos(user, page, pageSize);
	}
}
