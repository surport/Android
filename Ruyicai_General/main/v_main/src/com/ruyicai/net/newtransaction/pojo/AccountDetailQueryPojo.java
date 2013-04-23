package com.ruyicai.net.newtransaction.pojo;

/**
 * <b> 账户明细查询的pojo类<br>
 * </b> userno<br>
 * maxresult最大条数<br>
 * pageindex 第几页，比如第二页,pageindex=2<br>
 * sessionid <br>
 * type 1:充值，2:支付,3:派奖,4:提现<br>
 * 
 * @author miao
 * 
 */
public class AccountDetailQueryPojo {

	private String userno = "";
	private String maxresult = "";
	private String pageindex = "";
	private String sessionid = "";
	private String phonenum = "";
	private String transactiontype = "";
	private String type = "";

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMaxresult() {
		return maxresult;
	}

	public void setMaxresult(String maxresult) {
		this.maxresult = maxresult;
	}

	public String getTransactiontype() {
		return transactiontype;
	}

	public void setTransactiontype(String transactiontype) {
		this.transactiontype = transactiontype;
	}

	public String getUserno() {
		return userno;
	}

	public void setUserno(String userno) {
		this.userno = userno;
	}

	public String getPageindex() {
		return pageindex;
	}

	public void setPageindex(String pageindex) {
		this.pageindex = pageindex;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getPhonenum() {
		return phonenum;
	}

	public void setPhonenum(String phonenum) {
		this.phonenum = phonenum;
	}

}
