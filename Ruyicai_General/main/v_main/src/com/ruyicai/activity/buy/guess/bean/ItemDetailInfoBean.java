package com.ruyicai.activity.buy.guess.bean;

import java.util.List;

public class ItemDetailInfoBean {
	private String question = "";
	private String remainTime = "";
	private String prizePoolScore = "";
	private String id = "";
	private String answerId = "";
	private String answer = "";
	private boolean isSelected = false;
	private String prizeScore = "";
	public String getPrizeScore() {
		return prizeScore;
	}
	public void setPrizeScore(String prizeScore) {
		this.prizeScore = prizeScore;
	}
	private List<ItemOptionBean> optionList= null;
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public String getRemainTime() {
		return remainTime;
	}
	public void setRemainTime(String remainTime) {
		this.remainTime = remainTime;
	}
	public String getPrizePoolScore() {
		return prizePoolScore;
	}
	public void setPrizePoolScore(String prizePoolScore) {
		this.prizePoolScore = prizePoolScore;
	}
	public String getAnswerId() {
		return answerId;
	}
	public void setAnswerId(String answerId) {
		this.answerId = answerId;
	}
	public List<ItemOptionBean> getOptionList() {
		return optionList;
	}
	public void setOptionList(List<ItemOptionBean> optionList) {
		this.optionList = optionList;
	}
	
}