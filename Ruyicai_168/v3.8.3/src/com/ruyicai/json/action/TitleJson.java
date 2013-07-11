package com.ruyicai.json.action;

import org.json.JSONException;
import org.json.JSONObject;

public class TitleJson {
	public static final String TITLE = "title";
	public static final String TIME = "activityTime";
	public static final String INTRODUCE = "introduce";
	public static final String ISEND = "isEnd";// 1代表结束
	public static final String ID = "activityId";
	public JSONObject json = null;
	public ContentJson infoJson;
	String title;
	String time;
	String introduce;
	String isEnd;
	String id;

	public TitleJson(JSONObject json) {
		this.json = json;
		jsonToString();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public boolean getIsEnd() {
		if (isEnd.equals("1")) {
			return true;
		} else {
			return false;
		}
	}

	public void setIsEnd(String isEnd) {
		this.isEnd = isEnd;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 解析jso串
	 */
	public void jsonToString() {
		try {
			title = json.getString(TITLE);
			time = json.getString(TIME);
			introduce = json.getString(INTRODUCE);
			isEnd = json.getString(ISEND);
			id = json.getString(ID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
