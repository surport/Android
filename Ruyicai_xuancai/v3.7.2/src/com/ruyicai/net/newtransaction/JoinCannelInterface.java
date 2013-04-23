/**
 * 
 */
package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * @author Administrator
 * 
 */
public class JoinCannelInterface {
	private static String COMMAND = "betLot";
	private static JoinCannelInterface instance = null;

	private JoinCannelInterface() {
	}

	public synchronized static JoinCannelInterface getInstance() {
		if (instance == null) {
			instance = new JoinCannelInterface();
		}
		return instance;
	}

	/**
	 * 合买详情的方法
	 */
	public static String joincanel(String id, String userno, String phone) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.BETTYPE, "cancelCaselot");
			jsonProtocol.put(ProtocolManager.CASEID, id);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.PHONE_NUM, phone);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
}
