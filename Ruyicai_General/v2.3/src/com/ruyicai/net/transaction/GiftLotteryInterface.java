package com.ruyicai.net.transaction;

import java.util.Random;

import org.json.JSONObject;

import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.ProtocolManager;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.URLEncoder;

public class GiftLotteryInterface {
	
	private static String COMMAND = "queryGiftLot";
	/**
	 * 客户端赠送彩票请求
	 * @param from_mobile_code  客户端手机号
	 * @param to_mobile_code    受赠方手机号
	 * @param bet_code          投注码
	 * @param amount            购彩金额
	 * @param sessionID
	 * @return
	 */
	public static String gift(String from_mobile_code, String to_mobile_code,
			String bet_code, String amount, String sessionid) {
		String action = "giftAttemper.do";
		String method = "gift";
		String reValue = "";
		try {
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			paras.put("from_mobile_code", from_mobile_code);
			paras.put("to_mobile_code", to_mobile_code);
			paras.put("bet_code", bet_code);
			paras.put("amount", amount);
			paras.put("channel", Constants.COOP_ID);
			para = URLEncoder.encode(paras.toString());

			reValue = InternetUtils.GetMethodOpenHttpConnect(Constants.SERVER_URL + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}
	/**
	 * 体彩赠送彩票请求
	 * @param from_mobile_code   客户端手机号
	 * @param to_mobile_code     受赠方手机号
	 * @param bet_code           投注码
	 * @param amount             购彩金额
	 * @param sessionID
	 * @return
	 */
	public static String giftTC(String from_mobile_code, String to_mobile_code,
			String lotNo, String bet_code, String lotMulti, String amount,
			String oneMoney, String note_code, String sessionid, String advice) {
		String action = "presentedlottery.do";
		String method = "presented";
		String reValue = "";
		try {
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("from_mobile_code", from_mobile_code);
			paras.put("to_mobile_code", to_mobile_code);
			paras.put("lotNo", lotNo);
			paras.put("bet_code", bet_code);
			paras.put("lotMulti", lotMulti);
			paras.put("amount", amount);
			paras.put("oneMoney", oneMoney);
			paras.put("note_code", note_code);
			paras.put("advice", advice);
			para = URLEncoder.encode(paras.toString());

			reValue = InternetUtils.GetMethodOpenHttpConnect(Constants.SERVER_URL + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 体彩客户端赠送彩票查询请求//已测试
	 * @param mobile_code
	 * @param sessionID
	 * @return
	 */
	public static String giftSelectTC(String mobileCode, String sessionid,
			String startLine, String stopLine) {
		String action = "TCallSelect.do";
		String method = "giftAllSelect";
		String reValue = "";
		try {
//			String para = "";
//			JSONObject paras = new JSONObject();
//			paras.put("mobileCode", mobileCode);
//			para = URLEncoder.encode(paras.toString());
//			String where = "";
//			JSONObject jwhere = new JSONObject();
//			jwhere.put("lotNo", "");
//			jwhere.put("batchCode", "");
//			jwhere.put("startDate", "");
//			jwhere.put("stopDate", "");
//			jwhere.put("startLine", startLine);
//			jwhere.put("stopLine", stopLine);
//			where = URLEncoder.encode(jwhere.toString());
//			reValue = InternetUtils.GetMethodOpenHttpConnect(Constants.SERVER_URL + action
//					+ ";jsessionid=" + sessionid + "?method=" + method
//					+ "&jsonString=" + para + "&jsonWhere=" + where);
			
			JSONObject jsonProtocol = ProtocolManager.getInstance().getDefaultJsonProtocol();
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			
			jsonProtocol.put(ProtocolManager.START_LINE,startLine);
			jsonProtocol.put(ProtocolManager.STOP_LINE, stopLine);
			jsonProtocol.put(ProtocolManager.ACCOUNT_DETAIL_TYPE, "gift");
			jsonProtocol.put(ProtocolManager.PHONE_NUM, mobileCode);
			
			
			reValue = InternetUtils.GetMethodOpenHttpConnectSecurity(Constants.LOT_SERVER,jsonProtocol.toString());
			

		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}

	/**
	 * 体彩客户端被赠送彩票查询请求//已测试
	 * @param mobile_code
	 * @param sessionID
	 * @return
	 */
	public static String giftedSelectTC(String mobileCode, String sessionid,
			String startLine, String stopLine) {
			String action = "TCallSelect.do";
			String method = "giftedAllSelect";
		String reValue = "";
		try {
//			String para = "";
//			JSONObject paras = new JSONObject();
//			paras.put("mobileCode", mobileCode);
//			para = URLEncoder.encode(paras.toString());
//
//			String where = "";
//			JSONObject jwhere = new JSONObject();
//			jwhere.put("lotNo", "");
//			jwhere.put("batchCode", "");
//			jwhere.put("startDate", "");
//			jwhere.put("stopDate", "");
//			jwhere.put("startLine", startLine);
//			jwhere.put("stopLine", stopLine);
//			where = URLEncoder.encode(jwhere.toString());
//			reValue = InternetUtils.GetMethodOpenHttpConnect(Constants.SERVER_URL + action
//					+ ";jsessionid=" + sessionid + "?method=" + method
//					+ "&jsonString=" + para + "&jsonWhere=" + where);
			JSONObject jsonProtocol = ProtocolManager.getInstance().getDefaultJsonProtocol();
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			
			jsonProtocol.put(ProtocolManager.START_LINE,startLine);
			jsonProtocol.put(ProtocolManager.STOP_LINE, stopLine);
			jsonProtocol.put(ProtocolManager.ACCOUNT_DETAIL_TYPE, "gifted");
			jsonProtocol.put(ProtocolManager.PHONE_NUM, mobileCode);
			
			
			reValue = InternetUtils.GetMethodOpenHttpConnectSecurity(Constants.LOT_SERVER,jsonProtocol.toString());
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}
}
