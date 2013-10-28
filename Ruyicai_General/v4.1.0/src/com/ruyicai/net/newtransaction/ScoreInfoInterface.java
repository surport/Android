package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class ScoreInfoInterface {
	private static String COMMAND = "jingCai";

	private static ScoreInfoInterface instance = null;

	private ScoreInfoInterface() {
	}

	public synchronized static ScoreInfoInterface getInstance() {
		if (instance == null) {
			instance = new ScoreInfoInterface();
		}
		return instance;
	}

	/**
	 * 充值的方法
	 */
	public static String getScore(String event, String type) {
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
	
	public static String getBeiDanScore(String event, String type) {
		String re = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, ProtocolManager.BEIDAN_COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, type);
			jsonProtocol.put(ProtocolManager.EVENT, event);
			re = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}
}
