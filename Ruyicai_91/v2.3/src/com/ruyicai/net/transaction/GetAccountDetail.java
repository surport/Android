package com.ruyicai.net.transaction;

import java.util.Random;

import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.ProtocolManager;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.URLEncoder;

public class GetAccountDetail {
	
	private static String COMMAND = "accountdetail";
	
	/**
	 * 客户端账户明细查询
	 * @param mobile_number
	 * @param sessionID
	 * @return
	 */
	public static String accountDetailSelect( String mobile_code, String startdate, String enddate, String type, String sessionid) {
		Log.e("", "mobile_code="+mobile_code);
		Log.e("", "startdate="+startdate);
		Log.e("", "enddate="+enddate);
		Log.e("", "type="+type);
		Log.e("", "mobile_code="+mobile_code);
		
		String action = "touzhu.do";
		String method = "accountDetailSelect";
		String reValue = "";
		try {
//			Random rdm = new Random();
//			int transctionId = rdm.nextInt();
//			String para = "";
//			JSONObject paras = new JSONObject();
//			paras.put("inputCharset", 2);
//			paras.put("version", "v2.0");
//			paras.put("language", 1);
//			paras.put("transctionid", transctionId);
//			paras.put("mobile_code", mobile_code);
//			paras.put("startdate", startdate);
//			paras.put("enddate", enddate);
//			paras.put("type", type);
//			para = URLEncoder.encode(paras.toString());
//
//			reValue = InternetUtils.GetMethodOpenHttpConnect(Constants.LOT_SERVER + action
//					+ ";jsessionid=" + sessionid + "?method=" + method
//					+ "&jsonString=" + para);
			
			JSONObject jsonProtocol = ProtocolManager.getInstance().getDefaultJsonProtocol();
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			
			jsonProtocol.put(ProtocolManager.START_DATE, startdate);
			jsonProtocol.put(ProtocolManager.END_DATE, enddate);
			jsonProtocol.put(ProtocolManager.ACCOUNT_DETAIL_TYPE, type);
			jsonProtocol.put(ProtocolManager.PHONE_NUM, mobile_code);
			
			
			reValue = InternetUtils.GetMethodOpenHttpConnectSecurity(Constants.LOT_SERVER,jsonProtocol.toString());
			
			
			
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}
}
