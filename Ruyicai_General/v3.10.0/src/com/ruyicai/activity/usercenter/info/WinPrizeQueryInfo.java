package com.ruyicai.activity.usercenter.info;

import java.io.Serializable;

public class WinPrizeQueryInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2237L;
	private String winAmount;
	private String batchCode;
	private String lotNo;
	private String lotName;
	private String betCode;
	private String cashTime;
	private String sellTime;
	private String bet_code;
	private String orderId;
	private String lotMulti;
	private String betNum;
	private String amount;
	private String betCodeHtml;
	private String wincode;
	private String json;

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getWincode() {
		return wincode;
	}

	public void setWincode(String wincode) {
		this.wincode = wincode;
	}

	public String getBetCodeHtml() {
		return betCodeHtml;
	}

	public void setBetCodeHtml(String betCodeHtml) {
		this.betCodeHtml = betCodeHtml;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getLotMulti() {
		return lotMulti;
	}

	public void setLotMulti(String lotMulti) {
		this.lotMulti = lotMulti;
	}

	public String getBetNum() {
		return betNum;
	}

	public void setBetNum(String betNum) {
		this.betNum = betNum;
	}

	public String getLotName() {
		return lotName;
	}

	public void setLotName(String lotName) {
		this.lotName = lotName;
	}

	public String getBet_code() {
		return bet_code;
	}

	public void setBet_code(String betCode) {
		bet_code = betCode;
	}

	public String getSellTime() {
		return sellTime;
	}

	public void setSellTime(String sellTime) {
		this.sellTime = sellTime;
	}

	public String getWinAmount() {
		return winAmount;
	}

	public void setWinAmount(String winAmount) {
		this.winAmount = winAmount;
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

	public String getCashTime() {
		return cashTime;
	}

	public void setCashTime(String cashTime) {
		this.cashTime = cashTime;
	}

}
