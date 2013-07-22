package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class WinAndPulsaward {
	private static final String TAG = "SoftwareUpdateInterface";

	private static String COMMAND = "select";
	private String TYPE = "buyCenter";

	private WinAndPulsaward() {

	}

	private static WinAndPulsaward instance = null;

	public synchronized static WinAndPulsaward getInstance() {
		if (instance == null) {
			instance = new WinAndPulsaward();
		}
		return instance;
	}

	/**
	 * 
	 * 今日开奖加奖信息 
	 * 彩钟状态
	 */
	public String winandpulsquarey() {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, TYPE);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

}
