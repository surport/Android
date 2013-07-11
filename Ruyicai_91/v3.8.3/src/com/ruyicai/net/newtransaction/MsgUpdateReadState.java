package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * 站内信更新已读
 * 
 * @author win
 * 
 */
public class MsgUpdateReadState {
	private static String COMMAND = "feedback";

	private static MsgUpdateReadState instance = null;

	private MsgUpdateReadState() {
	}

	public synchronized static MsgUpdateReadState getInstance() {
		if (instance == null) {
			instance = new MsgUpdateReadState();
		}
		return instance;
	}

	public static String updateReadState(String id) {
		String re = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.TYPE, "updateReadState");
			jsonProtocol.put("id", id);
			re = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}
}
