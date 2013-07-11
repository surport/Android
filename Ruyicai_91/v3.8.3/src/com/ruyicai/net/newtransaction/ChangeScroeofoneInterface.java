package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class ChangeScroeofoneInterface {
	private static String COMMAND = "score";

	private static ChangeScroeofoneInterface instance = null;

	private ChangeScroeofoneInterface() {

	}

	public synchronized static ChangeScroeofoneInterface getInstance() {
		if (instance == null) {
			instance = new ChangeScroeofoneInterface();
		}
		return instance;
	}

	public JSONObject scoreinformation(String userno) {
		String result = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE,
					"transMoneyNeedScores");
			jsonProtocol.put(ProtocolManager.USERNO, userno);

			result = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
			return new JSONObject(result);

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

}
