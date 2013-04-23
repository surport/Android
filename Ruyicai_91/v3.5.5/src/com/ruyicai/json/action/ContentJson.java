package com.ruyicai.json.action;

import org.json.JSONException;
import org.json.JSONObject;

public class ContentJson {
	public static final String TITLE = "title";
	public static final String CONTENT = "content";
	public static final String URL = "url";

    public JSONObject json = null;
    private String title;
    public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	private String content;
    private String url;
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
			url=json.getString(URL);
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
