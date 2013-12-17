package com.ruyicai.activity.buy.guess.bean;

public class ItemInfoBean {
	/**
	 * 竞猜ID
	 */
	private String id = "";

	/**
	 * 题目
	 */
	private String title = "";

	/**
	 * 题目详情
	 */
	private String detail = "";

	/**
	 * 结束状态
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
	 * 剩余秒数
	 */
	private Long timeRemaining = 0L;

	/**
	 * 奖池积分
	 */
	private String prizePoolScore = "";

	/**
	 * 我投入的积分
	 */
	private String payScore = "";
	
	/**
	 * 是否中奖
	 */
	private String isWin = "";

	public String getIsWin() {
		return isWin;
	}

	public void setIsWin(String isWin) {
		this.isWin = isWin;
	}

	public String getPayScore() {
		return payScore;
	}

	public void setPayScore(String payScore) {
		this.payScore = payScore;
	}

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
