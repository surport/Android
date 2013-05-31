package com.ruyicai.net.newtransaction.pojo;

public class RechargePojo {
	private String userno = "";
	private String amount = "";
	private String cardtype = "";
	private String rechargetype = "";
	private String cardno = "";
	private String cardpwd = "";
	private String sessionid = "";
	private String name = "";
	private String certid = "";
	private String addressname = "";
	private String phonenum = "";
	private String iswhite = "";
	private String bankaddress = "";
	private String bankName = "";
	private String bankAccount = "";
	
	private String bankId = ""; //add by yejc 20130527
	private String mobileId = "";

	public String getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
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

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCardtype() {
		return cardtype;
	}

	public void setCardtype(String cardtype) {
		this.cardtype = cardtype;
	}

	public String getRechargetype() {
		return rechargetype;
	}

	public void setRechargetype(String rechargetype) {
		this.rechargetype = rechargetype;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getCardpwd() {
		return cardpwd;
	}

	public void setCardpwd(String cardpwd) {
		this.cardpwd = cardpwd;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCertid() {
		return certid;
	}

	public void setCertid(String certid) {
		this.certid = certid;
	}

	public String getAddressname() {
		return addressname;
	}

	public void setAddressname(String addressname) {
		this.addressname = addressname;
	}

	public void setIswhite(String iswhite) {
		this.iswhite = iswhite;
	}

	public String getIsIswhite() {
		return iswhite;
	}

	public void setBankaddress(String bankaddress) {
		this.bankaddress = bankaddress;
	}

	public String getBankaddress() {
		return bankaddress;
	}

	/**add by yejc 20130527 start*/
	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getMobileId() {
		return mobileId;
	}

	public void setMobileId(String mobileId) {
		this.mobileId = mobileId;
	}
	
	/**add by yejc 20130527 end*/

}
