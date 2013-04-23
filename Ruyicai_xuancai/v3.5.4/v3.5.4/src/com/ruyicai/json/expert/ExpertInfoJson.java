package com.ruyicai.json.expert;

import org.json.JSONException;
import org.json.JSONObject;

public class ExpertInfoJson {
	public static final String TITLE = "title";
	public static final String CODE = "messageCode";
	public static final String TOPHONE = "toPhone";
	public static final String CONTENT = "content";
	public static final String ALERTMSG = "alertMessage";
	public static final String BTNTEXT = "buttonText";

	public JSONObject json = null;
	private String title;
	private String code;
	private String toPhone;
	private String content;
	private String alertMsg;
	private String btnText;

	public ExpertInfoJson(JSONObject json) {
		this.json = json;
		jsonToString();
	}

	/**
	 * 解析jso串
	 */
	public void jsonToString() {
		try {
			title = json.getString(TITLE);
			code = json.getString(CODE);
			toPhone = json.getString(TOPHONE);
			content = json.getString(CONTENT);
			alertMsg = json.getString(ALERTMSG);
			btnText = json.getString(BTNTEXT);
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getToPhone() {
		return toPhone;
	}

	public void setToPhone(String toPhone) {
		this.toPhone = toPhone;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getAlsetMsg() {
		return alertMsg;
	}

	public void setAlsetMsg(String alertMsg) {
		this.alertMsg = alertMsg;
	}

	public String getBtnText() {
		return btnText;
	}

	public void setBtnText(String btnText) {
		this.btnText = btnText;
	}
}
