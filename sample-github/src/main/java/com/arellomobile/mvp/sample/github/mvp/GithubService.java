package com.arellomobile.mvp.sample.github.mvp;

import com.arellomobile.mvp.sample.github.app.GithubApi;
import com.arellomobile.mvp.sample.github.mvp.models.Repository;
import com.arellomobile.mvp.sample.github.mvp.models.User;

import java.util.List;

import rx.Observable;

/**
 * Date: 20.09.2016
 * Time: 20:14
 *
 * @author Yuri Shmakov
 */

public class GithubService {

	private GithubApi githubApi;

	public GithubService(GithubApi githubApi) {
		this.githubApi = githubApi;
	}


	public Observable<User> signIn(String token) {
		return githubApi.signIn(token);
	}

	public Observable<List<Repository>> getUserRepos(String user, int page, Integer pageSize) {
		return githubApi.getUserRepos(user, page, pageSize);
	}
}
