package com.ruyicai.json.action;

import org.json.JSONException;
import org.json.JSONObject;

public class ContentJson {
	public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public JSONObject json = null;
	private String title;
	private String content;

	public ContentJson(JSONObject json) {
		this.json = json;
		jsonToString();
	}

	/**
	 * 解析jso串
	 */
	public void jsonToString() {
		try {
			title = json.getString(TITLE);
			content = json.getString(CONTENT);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
