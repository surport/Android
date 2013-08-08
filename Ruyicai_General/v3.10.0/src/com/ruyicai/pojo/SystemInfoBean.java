package com.ruyicai.pojo;

import java.io.Serializable;

public class SystemInfoBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String _id = "";
	private String content = "";
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
