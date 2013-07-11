package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class AgencyInterface {

	private static String COMMAND = "recharge";

	private static AgencyInterface instance = null;

	private AgencyInterface() {
	}

	public synchronized static AgencyInterface getInstance() {
		if (instance == null) {
			instance = new AgencyInterface();
		}
		return instance;
	}

	public JSONObject agency(String userno, String name, String toName,
			String amout, String password) {
		String result = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.RECHARGETYPE, "09");
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.PHONENUM, name);
			jsonProtocol.put(ProtocolManager.TOMOBILECODE, toName);
			jsonProtocol.put(ProtocolManager.AMOUNT, amout);
			jsonProtocol.put(ProtocolManager.PASSWORD, password);
			result = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
			return new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
