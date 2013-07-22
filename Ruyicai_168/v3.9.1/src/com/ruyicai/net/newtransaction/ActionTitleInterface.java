package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class ActionTitleInterface {
	private static String COMMAND = "information";
	private static ActionTitleInterface instance = null;

	private ActionTitleInterface() {
	}

	public synchronized static ActionTitleInterface getInstance() {
		if (instance == null) {
			instance = new ActionTitleInterface();
		}
		return instance;
	}

	/**
	 * 活动标题查询
	 */
	public static String titleQuery() {
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.NEWSTYPE, "activityTitle");

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
}
