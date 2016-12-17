package com.arellomobile.mvp.sample.github.app;

import java.util.List;

import com.arellomobile.mvp.sample.github.mvp.models.Repository;
import com.arellomobile.mvp.sample.github.mvp.models.User;
import com.arellomobile.mvp.sample.github.mvp.models.gson.SearchResult;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Date: 18.01.2016
 * Time: 12:07
 *
 * @author Yuri Shmakov
 */
public interface GithubApi {
	Integer PAGE_SIZE = 50;

	@GET("/user")
	Observable<User> signIn(@Header("Authorization") String token);

	@GET("/search/repositories?sort=stars&order=desc")
	Observable<SearchResult> search(@Query("q") String query);

	@GET("/users/{login}/repos")
	Observable<List<Repository>> getUserRepos(@Path("login") String login, @Query("page") int page, @Query("per_page") int pageSize);
}
