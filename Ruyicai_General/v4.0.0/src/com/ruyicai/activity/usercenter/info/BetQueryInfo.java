package com.ruyicai.activity.usercenter.info;

import java.io.Serializable;

import org.json.JSONObject;

public class BetQueryInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 28876L;

	public boolean isRepeatBuy() {
		return isRepeatBuy;
	}

	public void setRepeatBuy(boolean isRepeatBuy) {
		this.isRepeatBuy = isRepeatBuy;
	}

	private String amount;
	private String prizeAmt;
	private String batchCode;
	private String lotNo;
	private String betCode;
	private String ordertime;
	private String lotName;
	private String play;
	private String orderTime;
	private String lotMulti;
	private String bet_code;
	private String stateMemo = "";
	private String betNum = "";
	private String oneAmount = "";
	private String betCodeHtml = "";
	private String json = "";
	/**add by pengcx 20130609 start*/
	private String expectPrizeAmt="";
	
	public String getExpectPrizeAmt() {
		return expectPrizeAmt;
	}
	
	public void setExpectPrizeAmt(String expectPrizeAmt) {
		this.expectPrizeAmt = expectPrizeAmt;
	}
	/**add by pengcx 20130609 end*/

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getBetCodeHtml() {
		return betCodeHtml;
	}

	public void setBetCodeHtml(String betCodeHtml) {
		this.betCodeHtml = betCodeHtml;
	}

	public String getOneAmount() {
		return oneAmount;
	}

	public void setOneAmount(String oneAmount) {
		this.oneAmount = oneAmount;
	}

	public String getBetNum() {
		return betNum;
	}

	public void setBetNum(String betNum) {
		this.betNum = betNum;
	}

	public String getStateMemo() {
		return stateMemo;
	}

	public void setStateMemo(String stateMemo) {
		this.stateMemo = stateMemo;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	private String prizeState;
	private String win_code;
	private boolean isRepeatBuy;
	private String orderId = "";

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPrizeAmt() {
		return prizeAmt;
	}

	public void setPrizeAmt(String prizeAmt) {
		this.prizeAmt = prizeAmt;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public String getLotNo() {
		return lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	public String getBetCode() {
		return betCode;
	}

	public void setBetCode(String betCode) {
		this.betCode = betCode;
	}

	public String getOrdertime() {
		return ordertime;
	}

	public void setOrdertime(String ordertime) {
		this.ordertime = ordertime;
	}

	public String getLotName() {
		return lotName;
	}

	public void setLotName(String lotName) {
		this.lotName = lotName;
	}

	public String getPlay() {
		return play;
	}

	public void setPlay(String play) {
		this.play = play;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getLotMulti() {
		return lotMulti;
	}

	public void setLotMulti(String lotMulti) {
		this.lotMulti = lotMulti;
	}

	public String getBet_code() {
		return bet_code;
	}

	public void setBet_code(String bet_code) {
		this.bet_code = bet_code;
	}

	public String getPrizeState() {
		return prizeState;
	}

	public void setPrizeState(String prizeState) {
		this.prizeState = prizeState;
	}

	public String getWin_code() {
		return win_code;
	}

	public void setWin_code(String win_code) {
		this.win_code = win_code;
	}

}
