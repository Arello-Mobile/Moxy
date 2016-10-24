package com.arellomobile.mvp.sample.github.mvp.models;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Date: 18.01.2016
 * Time: 12:09
 *
 * @author Yuri Shmakov
 */
@DatabaseTable
public class User {

	public static class Column {
		public static final String ID = "_id";
		public static final String LOGIN = "login";
		public static final String AVATAR = "avatar";
		public static final String PUBLIC_REPOS = "public_repos";
		public static final String PUBLIC_GISTS = "public_gists";
		public static final String FOLLOWERS = "followers";
		public static final String FOLLOWING = "following";
	}

	@DatabaseField(columnName = Column.ID, id = true)
	private int mId;
	@DatabaseField(columnName = Column.LOGIN)
	private String mLogin;
	@DatabaseField(columnName = Column.AVATAR)
	private String mAvatarUrl;
	@DatabaseField(columnName = Column.PUBLIC_REPOS)
	private int mPublicRepos;
	@DatabaseField(columnName = Column.PUBLIC_GISTS)
	private int mPublicGists;
	@DatabaseField(columnName = Column.FOLLOWERS)
	private int mFollower;
	@DatabaseField(columnName = Column.FOLLOWING)
	private int mFollowing;

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		mId = id;
	}

	public String getLogin() {
		return mLogin;
	}

	public void setLogin(String login) {
		mLogin = login;
	}

	public String getName() {
		return mLogin;
	}

	public void setName(String login) {
		mLogin = login;
	}

	public String getAvatarUrl() {
		return mAvatarUrl;
	}

	public void setAvatarUrl(String avatarUrl) {
		mAvatarUrl = avatarUrl;
	}

	public int getPublicRepos() {
		return mPublicRepos;
	}

	public void setPublicRepos(int publicRepos) {
		mPublicRepos = publicRepos;
	}

	public int getPublicGists() {
		return mPublicGists;
	}

	public void setPublicGists(int publicGists) {
		mPublicGists = publicGists;
	}

	public int getFollower() {
		return mFollower;
	}

	public void setFollower(int follower) {
		mFollower = follower;
	}

	public int getFollowing() {
		return mFollowing;
	}

	public void setFollowing(int following) {
		mFollowing = following;
	}
}
