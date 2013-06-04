package com.ruyicai.net.newtransaction.pojo;

import java.io.Serializable;

/**
 *<b> 投注和赠送的pojo类</b>
 * @author miao
 *
 */
public class BetAndGiftPojo implements Serializable{
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
	private String infoway="";
	private int amt=2;
	private String issuper="";
	
	private String totalAmt="";
	private String oneAmount="";
	private String safeAmt="";//保底金额
	private String buyAmt="";//认购金额
	private String commisionRation="";//提成比例
	private String visibility="";
	private String minAmt="";//最低认购金额
	private String description="";//方案描述
	
	
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
	
}
