package com.ruyicai.activity.info;

import java.util.ArrayList;
import java.util.List;

public class ContentList {
	private String content;

	private List<String> contentList = new ArrayList<String>(); // 解析之后的内容list,分为字符串与注码json,按顺序

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
		getOneText(content);
	}

	public List<String> getContentList() {
		return contentList;
	}

	public void setContentList(List<String> contentList) {
		this.contentList = contentList;

	}

	/**
	 * 根据{ 或者}截取一串字符串
	 * 
	 * @param str
	 */
	void getOneText(String str) {
		if (str == null || str.equals("")) {
			return;
		}
		int index = str.indexOf("{");
		if (index == -1) {
			// 无{符号了
			contentList.add(str);
			return;
		}
		int _end = str.indexOf("}");
		if (index == 0) {
			// 这个就是{开头的
			contentList.add(str.substring(0, _end + 1));
			getOneText(str.substring(_end + 1));
		} else {
			contentList.add(str.substring(0, index));
			getOneText(str.substring(index));
		}

	}

}
