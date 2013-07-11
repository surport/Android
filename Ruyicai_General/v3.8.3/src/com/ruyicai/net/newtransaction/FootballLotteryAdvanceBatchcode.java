package com.ruyicai.net.newtransaction;

import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class FootballLotteryAdvanceBatchcode {

	private static String COMMAND = "QueryLot";

	private static FootballLotteryAdvanceBatchcode instance;

	private FootballLotteryAdvanceBatchcode() {

	}

	public synchronized static FootballLotteryAdvanceBatchcode getInstance() {
		if (instance == null) {
			instance = new FootballLotteryAdvanceBatchcode();
		}
		return instance;
	}

	/**
	 * 提交服务器反馈内容
	 */
	public String getAdvanceBatchCodeList(String Lotno) {
		String reValue = "";
		try {

			JSONObject jsonProtocol = ProtocolManager.getInstance()
					.getDefaultJsonProtocol();
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.TYPE, "zcIssue");
			jsonProtocol.put(ProtocolManager.LOTNO, Lotno);
			reValue = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (Exception e) {
		}
		return reValue;
	}
}
