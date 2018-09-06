package com.arellomobile.mvp.sample.github.mvp.models.gson;

import com.arellomobile.mvp.sample.github.mvp.models.Repository;

import java.util.List;

/**
 * Date: 18.01.2016
 * Time: 12:13
 *
 * @author Yuri Shmakov
 */
public class SearchResult {
	private int totalCount;
	private boolean incompleteResults;
	private List<Repository> repositories;

	public List<Repository> getRepositories() {
		return repositories;
	}
}