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
public class MissInterface {

	private static String COMMAND = "QueryLot";
	private static MissInterface instance = null;

	private MissInterface() {
	}

	public static synchronized MissInterface getInstance() {
		if (instance == null) {
			instance = new MissInterface();
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
			jsonProtocol.put(ProtocolManager.TYPE, "missValue");
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
