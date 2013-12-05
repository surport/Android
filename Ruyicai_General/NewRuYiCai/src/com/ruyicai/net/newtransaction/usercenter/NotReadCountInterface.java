package com.ruyicai.net.newtransaction.usercenter;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.ruyicai.constant.Constants;
import com.ruyicai.constant.ShellRWConstants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;
import com.ruyicai.util.RWSharedPreferences;

/**
 * 留言未读数量接口
 * 
 * @author PengCX
 * 
 */
public class NotReadCountInterface {
	private static String COMMAND = "letter";

	private static NotReadCountInterface instance = null;

	private NotReadCountInterface() {

	}

	public synchronized static NotReadCountInterface getInstance() {
		if (instance == null) {
			instance = new NotReadCountInterface();
		}
		return instance;
	}

	public String getNotReadCount(Context context) {
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, "notReadCount");
			RWSharedPreferences shellRW = new RWSharedPreferences(context,
					ShellRWConstants.SHAREPREFERENCESNAME);
			jsonProtocol.put(ProtocolManager.USERNO,
					shellRW.getStringValue(ShellRWConstants.USERNO));

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";

	}
}
