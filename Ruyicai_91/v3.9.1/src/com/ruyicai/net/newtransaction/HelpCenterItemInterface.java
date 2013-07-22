package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class HelpCenterItemInterface {
	/**
	 * 帮助中心子目录接口
	 * 
	 * @author miao
	 * 
	 */

	private static String COMMAND = "information";

	private static HelpCenterItemInterface instance = null;

	private HelpCenterItemInterface() {
	}

	public synchronized static HelpCenterItemInterface getInstance() {
		if (instance == null) {
			instance = new HelpCenterItemInterface();
		}
		return instance;
	}

	/**
	 * 帮助中心方法
	 * 
	 */
	public JSONObject accountDetailQuery(String id) {
		String result = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.NEWSTYPE, "helpCenterContent");
			jsonProtocol.put("id", id);
			result = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
			return new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
