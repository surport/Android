package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class NewInformationInterface {

	private static String COMMAND = "information";
	public static NewInformationInterface instance = null;

	private NewInformationInterface() {

	}

	public static NewInformationInterface getInstance() {
		if (instance == null) {
			instance = new NewInformationInterface();
		}
		return instance;
	}

	/**
	 * 获取新闻标题
	 * 
	 * @param type
	 * @return
	 */
	public static String getInformationTitle(String type, String lotno) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.INFORMATION_TYPE, type);
			jsonProtocol.put(ProtocolManager.LOTNO, lotno);
			jsonProtocol.put(ProtocolManager.NEWSTYPE, "title");

			// Log.e("jsonProtocol-======", jsonProtocol.toString());
			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

}
