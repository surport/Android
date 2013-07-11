package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class TopNewsInformationInterface {
	private static String COMMAND = "information";
	private static TopNewsInformationInterface instance = null;

	private TopNewsInformationInterface() {
	}

	public synchronized static TopNewsInformationInterface getInstance() {
		if (instance == null) {
			instance = new TopNewsInformationInterface();
		}
		return instance;
	}

	/**
	 * 活动标题查询
	 */
	public static String informationQuery() {
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.NEWSTYPE, "topNews");

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
}
