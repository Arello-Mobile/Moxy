package com.arellomobile.mvp.sample.github.app;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;

/**
 * Date: 18.01.2016
 * Time: 14:11
 *
 * @author Yuri Shmakov
 */
public class GithubError extends Throwable {
	public GithubError(ResponseBody responseBody) {
		super(getMessage(responseBody));
	}

	private static String getMessage(ResponseBody responseBody) {
		try {
			return new JSONObject(responseBody.string()).optString("message");
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

		return "Unknown exception";
	}
}
