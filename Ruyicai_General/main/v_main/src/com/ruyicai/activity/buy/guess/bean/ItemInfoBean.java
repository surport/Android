package com.ruyicai.activity.buy.guess.bean;

public class ItemInfoBean {
	/**
	 * 竞猜编号
	 */
	private String id = "";
	
	/**
	 * 竞猜标题
	 */
	private String title = "";
	
	/**
	 * 竞猜题目描述
	 */
	private String detail = "";
	
	/**
	 * 参与状态
	 */
	private String participate = "";
	
	/**
	 * 结束状态
	 */
	private String endState = "";
	
	/**
	 * 开奖状态
	 */
	private String lotteryState = "";
	
	/**
	 * 离截止时间剩余的秒数
	 */
	private Long timeRemaining = 0L;
	
	/**
	 * 奖池积分
	 */
	private String prizePoolScore = "";
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getParticipate() {
		return participate;
	}
	public void setParticipate(String participate) {
		this.participate = participate;
	}
	public String getEndState() {
		return endState;
	}
	public void setEndState(String endState) {
		this.endState = endState;
	}
	public String getLotteryState() {
		return lotteryState;
	}
	public void setLotteryState(String lotteryState) {
		this.lotteryState = lotteryState;
	}
	public Long getTimeRemaining() {
		return timeRemaining;
	}
	public void setTimeRemaining(Long timeRemaining) {
		this.timeRemaining = timeRemaining;
	}
	public String getPrizePoolScore() {
		return prizePoolScore;
	}
	public void setPrizePoolScore(String prizePoolScore) {
		this.prizePoolScore = prizePoolScore;
	}
	
}
