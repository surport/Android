package com.ruyicai.pojo;

/**
 * 赠送信息实体类
 * @author haojie
 *
 */
public class GiftDetail {
	public String play_name = "";
	public String betcode = "";
	public String to_mobile_code = "";
	public String term_begin_datetime = "";
	public String sell_term_code = "";
	public String amount ="";
	public String maxLine="1";
	
	public String getPlay_name() {
		return play_name;
	}
	public void setPlay_name(String playName) {
		play_name = playName;
	}
	public String getBetcode() {
		return betcode;
	}
	public void setBetcode(String betcode) {
		this.betcode = betcode;
	}
	public String getTo_mobile_code() {
		return to_mobile_code;
	}
	public void setTo_mobile_code(String toMobileCode) {
		to_mobile_code = toMobileCode;
	}
	public String getTerm_begin_datetime() {
		return term_begin_datetime;
	}
	public void setTerm_begin_datetime(String termBeginDatetime) {
		term_begin_datetime = termBeginDatetime;
	}
	public String getSell_term_code() {
		return sell_term_code;
	}
	public void setSell_term_code(String sellTermCode) {
		sell_term_code = sellTermCode;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getMaxLine() {
		return maxLine;
	}
	public void setMaxLine(String maxLine) {
		this.maxLine = maxLine;
	}
	
}
