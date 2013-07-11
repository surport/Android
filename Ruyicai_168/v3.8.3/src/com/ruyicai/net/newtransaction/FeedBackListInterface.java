package com.ruyicai.net.newtransaction;

import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * 获取反馈列表
 * 
 * @author miao
 */
public class FeedBackListInterface {

	private static String COMMAND = "feedback";

	private static FeedBackListInterface instance;

	private FeedBackListInterface() {

	}

	public synchronized static FeedBackListInterface getInstance() {
		if (instance == null) {
			instance = new FeedBackListInterface();
		}
		return instance;
	}

	/**
	 * 提交服务器反馈内容
	 */
	public static String getFeedbackList(String pageindex, String maxresult,
			String userno) {
		String reValue = "";
		try {

			JSONObject jsonProtocol = ProtocolManager.getInstance()
					.getDefaultJsonProtocol();
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.TYPE, "feedBack");
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.PAGEINDEX, pageindex);
			jsonProtocol.put(ProtocolManager.MAXRESULT, maxresult);
			reValue = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (Exception e) {
			// Log.e(TAG, "softwareupeate error");
		}
		return reValue;

	}
}
