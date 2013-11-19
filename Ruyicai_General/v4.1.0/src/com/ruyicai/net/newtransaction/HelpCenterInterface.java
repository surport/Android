package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class HelpCenterInterface {
	/**
	 * 帮助中心接口
	 * 
	 * @author miao
	 * 
	 */

	private static String COMMAND = "information";

	private static HelpCenterInterface instance = null;

	private HelpCenterInterface() {
	}

	public synchronized static HelpCenterInterface getInstance() {
		if (instance == null) {
			instance = new HelpCenterInterface();
		}
		return instance;
	}

	/**
	 * 帮助中心方法
	 * 
	 */
	public JSONObject accountDetailQuery(String type, int pageIndex) {
		String result = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.TYPE, type);
			jsonProtocol.put(ProtocolManager.PAGEINDEX,
					String.valueOf(pageIndex));
			jsonProtocol.put(ProtocolManager.MAXRESULT, "7");
			jsonProtocol.put(ProtocolManager.NEWSTYPE, "helpCenterTitle");
			result = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
			return new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
