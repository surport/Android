package com.ruyicai.net.newtransaction.usercenter;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class AutoGetScoresRules {
	private static String COMMAND = "information";

	private static AutoGetScoresRules instance = null;

	private AutoGetScoresRules() {

	}

	public synchronized static AutoGetScoresRules getInstance() {
		if (instance == null) {
			instance = new AutoGetScoresRules();
		}
		return instance;
	}

	public String getScoresRules() {
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.NEWSTYPE, "scoreRule");
			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}

}
