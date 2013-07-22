package com.ruyicai.activity.usercenter.info;

import java.io.Serializable;

public class TrackQueryInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String lotName;
	private String betCode;
	private String batchNum;
	private String state;
	private String orderTime;
	private String beginBatch;
	private String amount;
	private String lotMulti;
	private String lastNums;
	private String id;
	private String prizeEnd;
	private String lotno;
	private String isRepeatBuy;// true可以继续追号
	private String betTouCode;
	private String betNum;// 注数
	private String oneAmount;// 单注金额
	private String remainderAmount;

	public String getRemainderAmount() {
		return remainderAmount;
	}

	public void setRemainderAmount(String remainderAmount) {
		this.remainderAmount = remainderAmount;
	}

	public String getBetNum() {
		return betNum;
	}

	public void setBetNum(String betNum) {
		this.betNum = betNum;
	}

	public String getOneAmount() {
		return oneAmount;
	}

	public void setOneAmount(String oneAmount) {
		this.oneAmount = oneAmount;
	}

	public String getBetTouCode() {
		return betTouCode;
	}

	public void setBetTouCode(String betTouCode) {
		this.betTouCode = betTouCode;
	}

	public String getIsRepeatBuy() {
		return isRepeatBuy;
	}

	public void setIsRepeatBuy(String isRepeatBuy) {
		this.isRepeatBuy = isRepeatBuy;
	}

	public String getLotno() {
		return lotno;
	}

	public void setLotno(String lotno) {
		this.lotno = lotno;
	}

	public String getPrizeEnd() {
		return prizeEnd;
	}

	public void setPrizeEnd(String prizeEnd) {
		this.prizeEnd = prizeEnd;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLastNums() {
		return lastNums;
	}

	public void setLastNums(String lastNums) {
		this.lastNums = lastNums;
	}

	public String getAmount() {
		return amount;
	}

	public String getLotMulti() {
		return lotMulti;
	}

	public void setLotMulti(String lotMulti) {
		this.lotMulti = lotMulti;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBetCode() {
		return betCode;
	}

	public void setBetCode(String betCode) {
		this.betCode = betCode;
	}

	public String getLotName() {
		return lotName;
	}

	public void setLotName(String lotName) {
		this.lotName = lotName;
	}

	public String getBatchNum() {
		return batchNum;
	}

	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getBeginBatch() {
		return beginBatch;
	}

	public void setBeginBatch(String beginBatch) {
		this.beginBatch = beginBatch;
	}

}
