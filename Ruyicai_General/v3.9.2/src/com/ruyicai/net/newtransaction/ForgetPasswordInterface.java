package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.ProtocolManager;

public class ForgetPasswordInterface {
	private static String COMMAND = "updateUserInfo";
	private static ForgetPasswordInterface instance = null;

	private ForgetPasswordInterface() {
	}

	public static synchronized ForgetPasswordInterface getInstance() {
		if (instance == null) {
			instance = new ForgetPasswordInterface();
		}
		return instance;
	}

	public String forgetPasswordNet(String name, String phone) {
		// Log.v("betandgift","betandgift");
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.TYPE, "retrievePassword");
			jsonProtocol.put(ProtocolManager.PHONENUM, name);
			jsonProtocol.put(ProtocolManager.BINPHONENUM, phone);
			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
}
