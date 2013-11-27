package com.ruyicai.net.newtransaction.pojo;

/**
 * <b> 投注查询<br>
 * 中奖查询 <br>
 * 追号查询<br>
 * 赠送查询的pojo类</b><br>
 * userno 用户编号 登录成功返回的用户编号如00000001<br>
 * pageindex 当前页<br>
 * maxresult 每页包含条数<br>
 * lotno 彩种 彩种<br>
 * sessionid sessionid<br>
 * batchcode 期号 期号<br>
 * type 查询类型 type 中奖查询为win<br>
 * command 请求 command:QueryLot<br>
 * 
 * @author miao
 */
public class BetAndWinAndTrackAndGiftQueryPojo {
	private String userno = "";
	private String pageindex = "";
	private String maxresult = "";
	private String lotno = "";
	private String type = "";
	private String dateType = "";
	private String state = "";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPageindex() {
		return pageindex;
	}

	public void setPageindex(String pageindex) {
		this.pageindex = pageindex;
	}

	public String getMaxresult() {
		return maxresult;
	}

	public void setMaxresult(String maxresult) {
		this.maxresult = maxresult;
	}

	private String sessionid = "";
	private String batchcode = "";
	private String phonenum = "";

	public String getUserno() {
		return userno;
	}

	public void setUserno(String userno) {
		this.userno = userno;
	}

	public String getLotno() {
		return lotno;
	}

	public void setLotno(String lotno) {
		this.lotno = lotno;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getBatchcode() {
		return batchcode;
	}

	public void setBatchcode(String batchcode) {
		this.batchcode = batchcode;
	}

	public String getPhonenum() {
		return phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

}
