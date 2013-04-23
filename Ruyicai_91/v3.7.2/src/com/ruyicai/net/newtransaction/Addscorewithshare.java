package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class Addscorewithshare {
	private static String COMMAND = "updateUserInfo";

	private Addscorewithshare() {
	}

	private static Addscorewithshare instance = null;

	public synchronized static Addscorewithshare getInstance() {
		if (instance == null) {
			instance = new Addscorewithshare();
		}
		return instance;
	}

	/**
	 * 查询提现记录
	 * 
	 */
	public static String addscore(String userno, String dis, String source) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.TYPE, "addScore");
			jsonProtocol.put("scoreType", "9");
			jsonProtocol.put("source", source);
			jsonProtocol.put(ProtocolManager.DESCRIPTION, dis);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

}
