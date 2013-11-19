package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * 中奖排行联网接口
 * 
 * @author miao
 * 
 */
public class PrizeRankInterface {

	private static String COMMAND = "AllQuery";
	private static String TYPE = "prizeRank";

	private static PrizeRankInterface instance = null;

	private PrizeRankInterface() {
	}

	public synchronized static PrizeRankInterface getInstance() {
		if (instance == null) {
			instance = new PrizeRankInterface();
		}
		return instance;
	}

	/**
	 * 中奖排行联网
	 * 
	 * @return
	 */
	public String prizeRankQuery() {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.TYPE, TYPE);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

}
