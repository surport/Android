package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class FollowCanelInterface {
	private static String COMMAND = "autoJoin";
	private static FollowCanelInterface instance = null;

	private FollowCanelInterface() {
	}

	public synchronized static FollowCanelInterface getInstance() {
		if (instance == null) {
			instance = new FollowCanelInterface();
		}
		return instance;
	}

	/**
	 * 查询参与合买的方法
	 */
	public static String Joinfollowcanel(String id) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, "cancelAutoJoin");
			jsonProtocol.put("id", id);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

}
