package com.ruyicai.net;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.activity.join.JoinDingActivity.CustomizeInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.newtransaction.CustomizeInterface;
import com.ruyicai.util.ProtocolManager;

public class CustomizeInfoInterface {
	private static String COMMAND = "autoJoin";

	private static CustomizeInfoInterface instance = null;

	private CustomizeInfoInterface() {
	}

	public synchronized static CustomizeInfoInterface getInstance() {
		if (instance == null) {
			instance = new CustomizeInfoInterface();
		}
		return instance;
	}

	/**
	 * 充值的方法
	 */
	public static String customizeNet(String starterUserNo, String lotno) {
		String re = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE,
					"selectCaseLotStarterInfo");
			jsonProtocol.put(ProtocolManager.STARTER_USERNO, starterUserNo);
			jsonProtocol.put(ProtocolManager.LOTNO, lotno);
			re = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}
}
