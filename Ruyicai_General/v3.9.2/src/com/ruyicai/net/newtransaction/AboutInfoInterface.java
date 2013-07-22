package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * 关于软件查询
 * 
 * @author miao
 * 
 */
public class AboutInfoInterface {

	private static String COMMAND = "information";

	private static AboutInfoInterface instance = null;

	private AboutInfoInterface() {
	}

	public synchronized static AboutInfoInterface getInstance() {
		if (instance == null) {
			instance = new AboutInfoInterface();
		}
		return instance;
	}

	public JSONObject aboutInfoQuery() {
		String result = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.NEWSTYPE, "versionIntroduce");
			result = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
			return new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
