package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.ProtocolManager;

/**
 * 玩法介绍接口
 * 
 * @author Administrator
 * 
 */
public class LotnoGameInterface {
	private static String COMMAND = "information";
	private static LotnoGameInterface instance = null;

	private LotnoGameInterface() {
	}

	public static synchronized LotnoGameInterface getInstance() {
		if (instance == null) {
			instance = new LotnoGameInterface();
		}
		return instance;
	}

	/**
	 * 
	 */
	public String lotnoGame(String lotno) {
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.NEWSTYPE, "playIntroduce");
			jsonProtocol.put(ProtocolManager.LOTNO, lotno);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
}
