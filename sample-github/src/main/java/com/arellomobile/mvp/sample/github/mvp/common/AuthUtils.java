package com.arellomobile.mvp.sample.github.mvp.common;

/**
 * Date: 18.01.2016
 * Time: 15:02
 *
 * @author Yuri Shmakov
 */
public class AuthUtils {
	private static final String TOKEN = "token";

	public static String getToken() {
		return PrefUtils.getPrefs().getString(TOKEN, "");
	}

	public static void setToken(String token) {
		PrefUtils.getEditor().putString(TOKEN, token).commit();
	}
}
