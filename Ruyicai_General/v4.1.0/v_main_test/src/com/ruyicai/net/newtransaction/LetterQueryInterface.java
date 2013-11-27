package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class LetterQueryInterface {

	private static String COMMAND = "letter";
	private static LetterQueryInterface instance = null;

	private LetterQueryInterface() {
	}

	public synchronized static LetterQueryInterface getInstance() {
		if (instance == null) {
			instance = new LetterQueryInterface();
		}
		return instance;
	}

	/**
	 * 参与合买的方法
	 */
	public static String letter(String userno, String maxresult,
			String pageindex) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, "list");
			jsonProtocol.put(ProtocolManager.MAXRESULT, maxresult);
			jsonProtocol.put(ProtocolManager.PAGEINDEX, pageindex);
			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

}
