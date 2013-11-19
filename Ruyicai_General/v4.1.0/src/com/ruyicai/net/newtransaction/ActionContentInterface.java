package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class ActionContentInterface {
	private static String COMMAND = "information";
	private static ActionContentInterface instance = null;

	private ActionContentInterface() {
	}

	public synchronized static ActionContentInterface getInstance() {
		if (instance == null) {
			instance = new ActionContentInterface();
		}
		return instance;
	}

	/**
	 * 活动内容查询
	 */
	public static String contentQuery(String id) {
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.ID, id);
			jsonProtocol.put(ProtocolManager.NEWSTYPE, "activityContent");

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
}
