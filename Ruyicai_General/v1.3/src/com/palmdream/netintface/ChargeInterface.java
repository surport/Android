package com.palmdream.netintface;



import org.json.JSONException;
import org.json.JSONObject;

import com.palmdream.RuyicaiAndroid.PublicMethod;

public class ChargeInterface {
protected static String[] bankcharge = null;

	
	String error_code="00";
	String re;
	public String url;
	int iretrytimes=2;
	 JSONObject obj = null;
	    
	 /**
		 * 跳转类账户充值
		 * @param accesstype  接入方式     B表示web，W表示wap，C表示客户端
		 * @param mobile_code 登录的手机号码
		 * @param cardType    支付方式
		 * @param transaction_money   充值金额	(分为单位)
		 * @param bankId              银行标识
		 * @param expand              扩展参数
		 * @param sessionid
		 * @return
		 */
      public String phonebankcharge(String accesstype,String mobile_code,String cardType,String transaction_money,String bankId,String expand,String sessionid){
    	  
			while(iretrytimes<3&&iretrytimes>0){
				 re=jrtLot.wapAttemperRequest(accesstype, mobile_code, cardType, transaction_money+"00", bankId, expand, sessionid);
			try {
				obj = new JSONObject(re);
				error_code = obj.getString("error_code");
				PublicMethod.myOutput("---------------------"+error_code);
				iretrytimes=3;
				if(error_code.equals("000000")){
					url=obj.getString("requrl");
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				iretrytimes--;
				PublicMethod.myOutput("---------iretrytimes----------"+iretrytimes);
			}
	
		}
		PublicMethod.myOutput("---------iHttp.whetherChange----------"+iHttp.whetherChange);
//		陈晨 8.11 接 入点改变判断
		if (iretrytimes == 0 && iHttp.whetherChange == false) {
			iHttp.whetherChange = true;
			if (iHttp.conMethord == iHttp.CMWAP) {
				iHttp.conMethord = iHttp.CMNET;
			} else {
				iHttp.conMethord = iHttp.CMWAP;
			}
			iretrytimes =2;
			phonebankcharge(accesstype, mobile_code,
					cardType, transaction_money, bankId,
					expand, sessionid);
		 }		
    	  return error_code;
      }
      
      /**
  	 * 非跳转类账户充值
  	 * @param accesstype    接入方式     B表示web，W表示wap，C表示客户端
  	 * @param mobile_code   登录的手机号码
  	 * @param cardType      支付方式
  	 * @param transaction_money 充值金额	(分为单位)
  	 * @param totalAmount       卡面值
  	 * @param card_no           卡号
  	 * @param card_pwd          卡密码
  	 * @param bankId            银行标识
  	 * @param expand            扩展参数
  	 * @param sessionid
  	 * @return
  	 */
      public String phonecardcharge(String accesstype,String mobile_code,String cardType,String transaction_money,String totalAmount,String card_no,String card_pwd,String bankId,String expand,String sessionid){
    	  while(iretrytimes<3&&iretrytimes>0){
				 re=jrtLot.attemperRequest(accesstype, mobile_code, cardType, transaction_money+"00",totalAmount+"00", card_no,card_pwd,bankId, expand, sessionid);
			try {
				obj = new JSONObject(re);
				error_code = obj.getString("error_code");
				PublicMethod.myOutput("---------------------"+error_code);
				iretrytimes=3;
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				iretrytimes--;
				PublicMethod.myOutput("---------iretrytimes----------"+iretrytimes);
			}
		}
//		陈晨 8.11 接 入点改变判断	
		if (iretrytimes == 0 && iHttp.whetherChange == false) {
			iHttp.whetherChange = true;
			if (iHttp.conMethord == iHttp.CMWAP) {
				iHttp.conMethord = iHttp.CMNET;
			} else {
				iHttp.conMethord = iHttp.CMWAP;
			}
			iretrytimes =2;
			phonecardcharge(accesstype, mobile_code,
					cardType, transaction_money,totalAmount,
					card_no, card_pwd, bankId, expand,
					sessionid);
		 }	
    	  return error_code;
      }
}
