package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class ExplainInterface {
	public static String COMMAND = "jingCai";

	private static ExplainInterface instance = null;

	private ExplainInterface() {
	}

	public synchronized static ExplainInterface getInstance() {
		if (instance == null) {
			instance = new ExplainInterface();
		}
		return instance;
	}

	public static String getExplain(String event, String type) {
		String re = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, type);
			jsonProtocol.put(ProtocolManager.EVENT, event);
			re = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}
	/**
	 * 获得分析数据
	 * @param command
	 * @param event
	 * @param type
	 * @return
	 */
	public static String getExplain(String command,String event, String type) {
		String re = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, command);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, type);
			jsonProtocol.put(ProtocolManager.EVENT, event);
			re = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}
	
	/**add by yejc 20130422 start*/
	public static String getRecommendStr(String event, String type, 
			String pageindex, String maxresult) {
		String str = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, type);
			jsonProtocol.put(ProtocolManager.EVENT, event);
			jsonProtocol.put(ProtocolManager.PAGEINDEX, pageindex);
			jsonProtocol.put(ProtocolManager.MAXRESULT, maxresult);
			str = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return str;
	}
	/**add by yejc 20130422 end*/
}
