package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class ScoreListInterface {
	private static String COMMAND = "jingCai";

	private static ScoreListInterface instance = null;

	private ScoreListInterface() {
	}

	public synchronized static ScoreListInterface getInstance() {
		if (instance == null) {
			instance = new ScoreListInterface();
		}
		return instance;
	}

	/**
	 * 充值的方法
	 */
	public static String getScore(String type, String date, String reguestType) {
		String re = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REGUESTTYPE, reguestType);
			jsonProtocol.put(ProtocolManager.TYPE, type);
			jsonProtocol.put(ProtocolManager.DATE, date);
			re = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}
}
