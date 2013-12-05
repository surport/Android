package com.ruyicai.net.newtransaction.pojo;

import java.io.Serializable;

/**
 * <b> 投注和赠送的pojo类</b>
 * 
 * @author miao
 * 
 */
public class BetAndGiftPojo implements Serializable {
	private String userno = "";
	private String batchcode = "";
	private String batchnum = "";
	private String bet_code = "";
	private String lotno = "";
	private String lotmulti = "";
	private String sellway = "";
	private String amount = "";
	private String bettype = "";
	private String sessionid = "";
	private String to_mobile_code = "";
	private String advice = "";
	private String phonenum = "";
	private String infoway = "";
	private int amt = 2;// 彩票单价默认为2元，大乐透又追加投注
	private String issuper = "";

	private String totalAmt = "";
	private String oneAmount = "";
	private String safeAmt = "";// 保底金额
	private String buyAmt = "";// 认购金额
	private String commisionRation = "";// 提成比例
	private String visibility = "";
	private String minAmt = "";// 最低认购金额
	private String description = "";// 方案描述
	private String prizeend = "1";// 中奖后停止追号1代表中奖停止
	private String isSellWays = "";// 1代表多笔投注
	private boolean isZhui = false;
	private String subscribeInfo = "";
	private String isBetAfterIssue = "";
	private String zhushu;
	private String blessing;
	/**
	 * 用于足彩临时存放注码。不作为投注时的注码
	 */
	private String betCode = ""; 
	/**add by pengcx 20130609 start*/
	private String predictMoney="0";//预计奖金
	public String getPredictMoney() {
		return predictMoney;
	}

	public void setPredictMoney(String predictMoney) {
		this.predictMoney = predictMoney;
	}
	/**add by pengcx 20130609 end*/
	public String getBlessing() {
		return blessing;
	}

	public void setBlessing(String blessing) {
		this.blessing = blessing;
	}

	public String getZhushu() {
		return zhushu;
	}

	public void setZhushu(String zhushu) {
		this.zhushu = zhushu;
	}

	public boolean isZhui() {
		return isZhui;
	}

	public void setZhui(boolean isZhui) {
		this.isZhui = isZhui;
	}

	public String getIsSellWays() {
		return isSellWays;
	}

	public void setIsSellWays(String isSellWays) {
		this.isSellWays = isSellWays;
	}

	public String getPrizeend() {
		return prizeend;
	}

	public void setPrizeend(String prizeend) {
		this.prizeend = prizeend;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(String totalAmt) {
		this.totalAmt = totalAmt;
	}

	public String getOneAmount() {
		return oneAmount;
	}

	public void setOneAmount(String oneAmount) {
		this.oneAmount = oneAmount;
	}

	public String getSafeAmt() {
		return safeAmt;
	}

	public void setSafeAmt(String safeAmt) {
		this.safeAmt = safeAmt;
	}

	public String getBuyAmt() {
		return buyAmt;
	}

	public void setBuyAmt(String buyAmt) {
		this.buyAmt = buyAmt;
	}

	public String getCommisionRation() {
		return commisionRation;
	}

	public void setCommisionRation(String commisionRation) {
		this.commisionRation = commisionRation;
	}

	public String getVisibility() {
		return visibility;
	}

	public void setVisibility(String visibility) {
		this.visibility = visibility;
	}

	public String getMinAmt() {
		return minAmt;
	}

	public void setMinAmt(String minAmt) {
		this.minAmt = minAmt;
	}

	public String getIssuper() {
		return issuper;
	}

	public void setIssuper(String issuper) {
		this.issuper = issuper;
	}

	public int getAmt() {
		return amt;
	}

	public void setAmt(int amt) {
		this.amt = amt;
	}

	public String getPhonenum() {
		return phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

	public String getUserno() {
		return userno;
	}

	public void setUserno(String userno) {
		this.userno = userno;
	}

	public String getBatchcode() {
		return batchcode;
	}

	public void setBatchcode(String batchcode) {
		this.batchcode = batchcode;
	}

	public String getBatchnum() {
		return batchnum;
	}

	public void setBatchnum(String batchnum) {
		this.batchnum = batchnum;
	}

	public String getBet_code() {
		return bet_code;
	}

	public void setBet_code(String betCode) {
		bet_code = betCode;
	}

	public String getLotno() {
		return lotno;
	}

	public void setLotno(String lotno) {
		this.lotno = lotno;
	}

	public String getLotmulti() {
		return lotmulti;
	}

	public void setLotmulti(String lotmulti) {
		this.lotmulti = lotmulti;
	}

	public String getSellway() {
		return sellway;
	}

	public void setSellway(String sellway) {
		this.sellway = sellway;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBettype() {
		return bettype;
	}

	public void setBettype(String bettype) {
		this.bettype = bettype;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getTo_mobile_code() {
		return to_mobile_code;
	}

	public void setTo_mobile_code(String toMobileCode) {
		to_mobile_code = toMobileCode;
	}

	public String getAdvice() {
		return advice;
	}

	public void setAdvice(String advice) {
		this.advice = advice;
	}

	public void setInfoway(String infoway) {
		this.infoway = infoway;
	}

	public String getInfoway() {
		return infoway;
	}

	public String getSubscribeInfo() {
		return subscribeInfo;
	}

	public void setSubscribeInfo(String subscribeInfo) {
		this.subscribeInfo = subscribeInfo;
	}

	public String getIsBetAfterIssue() {
		return isBetAfterIssue;
	}

	public void setIsBetAfterIssue(String isBetAfterIssue) {
		this.isBetAfterIssue = isBetAfterIssue;
	}

	public String getBetCode() {
		return betCode;
	}

	public void setBetCode(String betCode) {
		this.betCode = betCode;
	}

}
