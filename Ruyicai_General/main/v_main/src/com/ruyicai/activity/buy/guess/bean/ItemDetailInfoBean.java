package com.ruyicai.activity.buy.guess.bean;

public class ItemDetailInfoBean {
	private String question = "";
	private String award = "";
	private String drawTime = "";
	private String id = "";
	private String[][] options = null;
	private String endState = "";
	private String answer = "";
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAward() {
		return award;
	}
	public void setAward(String award) {
		this.award = award;
	}
	public String getDrawTime() {
		return drawTime;
	}
	public void setDrawTime(String drawTime) {
		this.drawTime = drawTime;
	}
	public String[][] getOptions() {
		return options;
	}
	public void setOptions(String[][] options) {
		this.options = options;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEndState() {
		return endState;
	}
	public void setEndState(String endState) {
		this.endState = endState;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	
}