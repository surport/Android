package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.newtransaction.pojo.BetAndWinAndTrackAndGiftQueryPojo;
import com.ruyicai.util.ProtocolManager;

public class GiftReciveCodeInterface {

	private static String COMMAND = "betLot";

	private static GiftReciveCodeInterface instance = null;

	private GiftReciveCodeInterface() {
	}

	public synchronized static GiftReciveCodeInterface getInstance() {
		if (instance == null) {
			instance = new GiftReciveCodeInterface();
		}
		return instance;
	}

	/**
	 * 领取彩票获取验证码
	 * 
	 * @param giftQueryPojo
	 * @return
	 */
	public String giftCodeQuery(String id, String userno) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.BETTYPE,
					"receivePresentSecurityCode");
			jsonProtocol.put(ProtocolManager.GIFTID, id);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
}
