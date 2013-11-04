package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.ProtocolManager;

/**
 * 遗漏值接口
 * 
 * @author miao
 */
public class NMK3MissInterface {

	private static String COMMAND = "missValue";
	private static NMK3MissInterface instance = null;

	private NMK3MissInterface() {
	}

	public static synchronized NMK3MissInterface getInstance() {
		if (instance == null) {
			instance = new NMK3MissInterface();
		}
		return instance;
	}

	/**
	 * 
	 * @param betPojo
	 * @return
	 */
	public String betMiss(String lotno, String sellWay) {
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, "selectByKeys");
			jsonProtocol.put(ProtocolManager.LOTNO, lotno);
			jsonProtocol.put(ProtocolManager.SELLWAY, sellWay);
			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

}
