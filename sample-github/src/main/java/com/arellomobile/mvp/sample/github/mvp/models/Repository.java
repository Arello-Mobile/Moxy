package com.arellomobile.mvp.sample.github.mvp.models;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Date: 18.01.2016
 * Time: 12:12
 *
 * @author Yuri Shmakov
 */
@DatabaseTable
public class Repository implements Serializable {
	private static final long serialVersionUID = -4994763529881200165L;

	public static class Column {
		public static final String ID = "_id";
		public static final String NAME = "name";
		public static final String FULL_NAME = "full_name";
		public static final String DESC = "desc";
		public static final String URL = "url";
		public static final String HTML_URL = "html_url";
		public static final String OWNER = "owner";
		public static final String IS_FORK = "is_fork";
		public static final String IS_PRIVATE = "is_private";
		public static final String CREATED_AT = "created_at";
		public static final String UPDATED_AT = "updated_at";
		public static final String PUSHED_AT = "pushed_at";
		public static final String GIT_URL = "git_url";
		public static final String SSH_URL = "ssh_url";
		public static final String CLONE_URL = "clone_url";
		public static final String SVN_URL = "svn_url";
		public static final String HOMEPAGE = "homepage";
		public static final String SIZE = "size";
		public static final String STARS_COUNT = "stars_count";
		public static final String WATCHERS_COUNT = "watchers_count";
		public static final String LANGUAGE = "language";
		public static final String HAS_ISSUES = "has_issues";
		public static final String HAS_DOWNLOADS = "has_downloads";
		public static final String HAS_WIKI = "has_wiki";
		public static final String FORKS_COUNT = "forks_count";
		public static final String OPEN_ISSUES_COUNT = "open_issues_count";
		public static final String DEFAULT_BRANCH = "default_branch";
	}

	@DatabaseField(columnName = Column.ID, id = true)
	private int mId;
	@DatabaseField(columnName = Column.NAME)
	private String mName;
	@DatabaseField(columnName = Column.FULL_NAME)
	private String mFullName;
	@SerializedName("description")
	@DatabaseField(columnName = Column.DESC)
	private String mDesc;
	@DatabaseField(columnName = Column.URL)
	private String mUrl;
	@DatabaseField(columnName = Column.HTML_URL)
	private String mHtmlUrl;
	@DatabaseField(columnName = Column.OWNER, foreign = true)
	private transient User mOwner;
	@SerializedName("fork")
	@DatabaseField(columnName = Column.IS_FORK)
	private String mIsFork;
	@SerializedName("private")
	@DatabaseField(columnName = Column.IS_PRIVATE)
	private String mIsPrivate;
	@DatabaseField(columnName = Column.CREATED_AT, dataType = DataType.DATE)
	private Date mCreatedAt;
	@DatabaseField(columnName = Column.UPDATED_AT, dataType = DataType.DATE)
	private Date mUpdatedAt;
	@DatabaseField(columnName = Column.PUSHED_AT, dataType = DataType.DATE)
	private Date mPushedAt;
	@DatabaseField(columnName = Column.GIT_URL)
	private String mGitUrl;
	@DatabaseField(columnName = Column.SSH_URL)
	private String mSshUrl;
	@DatabaseField(columnName = Column.CLONE_URL)
	private String mCloneUrl;
	@DatabaseField(columnName = Column.SVN_URL)
	private String mSvnUrl;
	@DatabaseField(columnName = Column.HOMEPAGE)
	private String mHomepage;
	@DatabaseField(columnName = Column.SIZE)
	private long mSize;
	@SerializedName("stargazers_count")
	@DatabaseField(columnName = Column.STARS_COUNT)
	private int mStarsCount;
	@DatabaseField(columnName = Column.WATCHERS_COUNT)
	private int mWatchersCount;
	@DatabaseField(columnName = Column.LANGUAGE)
	private String mLanguage;
	@DatabaseField(columnName = Column.HAS_ISSUES)
	private boolean mHasIssues;
	@DatabaseField(columnName = Column.HAS_DOWNLOADS)
	private boolean mHasDownloads;
	@DatabaseField(columnName = Column.HAS_WIKI)
	private boolean mHasWiki;
	@DatabaseField(columnName = Column.FORKS_COUNT)
	private int mForksCount;
	@DatabaseField(columnName = Column.OPEN_ISSUES_COUNT)
	private int mOpenIssuesCount;
	@DatabaseField(columnName = Column.DEFAULT_BRANCH)
	private String mDefaultBranch;

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		mId = id;
	}

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		mName = name;
	}

	public String getFullName() {
		return mFullName;
	}

	public void setFullName(String fullName) {
		mFullName = fullName;
	}

	public String getDesc() {
		return mDesc;
	}

	public void setDesc(String desc) {
		mDesc = desc;
	}

	public String getUrl() {
		return mUrl;
	}

	public void setUrl(String url) {
		mUrl = url;
	}

	public String getHtmlUrl() {
		return mHtmlUrl;
	}

	public void setHtmlUrl(String htmlUrl) {
		mHtmlUrl = htmlUrl;
	}

	public User getOwner() {
		return mOwner;
	}

	public void setOwner(User owner) {
		mOwner = owner;
	}

	public String isFork() {
		return mIsFork;
	}

	public void setIsFork(String isFork) {
		mIsFork = isFork;
	}

	public String getIsPrivate() {
		return mIsPrivate;
	}

	public void setIsPrivate(String isPrivate) {
		mIsPrivate = isPrivate;
	}

	public Date getCreatedAt() {
		return mCreatedAt;
	}

	public void setCreatedAt(Date createdAt) {
		mCreatedAt = createdAt;
	}

	public Date getUpdatedAt() {
		return mUpdatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		mUpdatedAt = updatedAt;
	}

	public Date getPushedAt() {
		return mPushedAt;
	}

	public void setPushedAt(Date pushedAt) {
		mPushedAt = pushedAt;
	}

	public String getGitUrl() {
		return mGitUrl;
	}

	public void setGitUrl(String gitUrl) {
		mGitUrl = gitUrl;
	}

	public String getSshUrl() {
		return mSshUrl;
	}

	public void setSshUrl(String sshUrl) {
		mSshUrl = sshUrl;
	}

	public String getCloneUrl() {
		return mCloneUrl;
	}

	public void setCloneUrl(String cloneUrl) {
		mCloneUrl = cloneUrl;
	}

	public String getSvnUrl() {
		return mSvnUrl;
	}

	public void setSvnUrl(String svnUrl) {
		mSvnUrl = svnUrl;
	}

	public String getHomepage() {
		return mHomepage;
	}

	public void setHomepage(String homepage) {
		mHomepage = homepage;
	}

	public long getSize() {
		return mSize;
	}

	public void setSize(long size) {
		mSize = size;
	}

	public int getStarsCount() {
		return mStarsCount;
	}

	public void setStarsCount(int starsCount) {
		mStarsCount = starsCount;
	}

	public int getWatchersCount() {
		return mWatchersCount;
	}

	public void setWatchersCount(int watchersCount) {
		mWatchersCount = watchersCount;
	}

	public String getLanguage() {
		return mLanguage;
	}

	public void setLanguage(String language) {
		mLanguage = language;
	}

	public boolean isHasIssues() {
		return mHasIssues;
	}

	public void setHasIssues(boolean hasIssues) {
		mHasIssues = hasIssues;
	}

	public boolean isHasDownloads() {
		return mHasDownloads;
	}

	public void setHasDownloads(boolean hasDownloads) {
		mHasDownloads = hasDownloads;
	}

	public boolean isHasWiki() {
		return mHasWiki;
	}

	public void setHasWiki(boolean hasWiki) {
		mHasWiki = hasWiki;
	}

	public int getForksCount() {
		return mForksCount;
	}

	public void setForksCount(int forksCount) {
		mForksCount = forksCount;
	}

	public int getOpenIssuesCount() {
		return mOpenIssuesCount;
	}

	public void setOpenIssuesCount(int openIssuesCount) {
		mOpenIssuesCount = openIssuesCount;
	}

	public String getDefaultBranch() {
		return mDefaultBranch;
	}

	public void setDefaultBranch(String defaultBranch) {
		mDefaultBranch = defaultBranch;
	}
}