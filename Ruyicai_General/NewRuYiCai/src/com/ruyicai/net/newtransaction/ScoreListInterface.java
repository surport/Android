package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class ScoreListInterface {
	private static String COMMAND = "jingCai";

	private static ScoreListInterface instance = null;

	private ScoreListInterface() {
	}

	public synchronized static ScoreListInterface getInstance() {
		if (instance == null) {
			instance = new ScoreListInterface();
		}
		return instance;
	}

	/**
	 * 充值的方法
	 */
	public static String getScore(String type, String date, String reguestType) {
		String re = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, reguestType);
			jsonProtocol.put(ProtocolManager.TYPE, type);
			jsonProtocol.put(ProtocolManager.DATE, date);
			re = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}
	
	/**
	 * 获取进行中的比赛
	 */
	public static String getCurrentScore(String command, String reguestType) {
		String re = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, command);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, reguestType);
			re = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}
	
	/**
	 * 获取北单即时比分
	 */
	public static String getBeiDanScore(String type, String reguestType, 
			String lotno, String batchCode) {
		String re = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, ProtocolManager.BEIDAN_COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, reguestType);
			jsonProtocol.put(ProtocolManager.TYPE, type);
//			jsonProtocol.put(ProtocolManager.DATE, date);
			jsonProtocol.put(ProtocolManager.LOTNO, lotno);
			jsonProtocol.put(ProtocolManager.BATCHCODE, batchCode);
			re = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}
}
