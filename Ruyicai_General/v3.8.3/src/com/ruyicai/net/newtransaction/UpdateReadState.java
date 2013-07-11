package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class UpdateReadState {
	private static String COMMAND = "letter";

	private static UpdateReadState instance = null;

	private UpdateReadState() {
	}

	public synchronized static UpdateReadState getInstance() {
		if (instance == null) {
			instance = new UpdateReadState();
		}
		return instance;
	}

	public static String updateReadState(String id) {
		String re = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, "updateReadState");
			jsonProtocol.put("id", id);
			re = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}
}
