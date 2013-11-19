package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class QueryJoinFollowInterface {
	private static String COMMAND = "autoJoin";
	private static QueryJoinFollowInterface instance = null;

	private QueryJoinFollowInterface() {
	}

	public synchronized static QueryJoinFollowInterface getInstance() {
		if (instance == null) {
			instance = new QueryJoinFollowInterface();
		}
		return instance;
	}

	/**
	 * 查询参与合买的方法
	 */
	public static String queryLotJoinfollow(String userno, String phonenum,
			String newPage, String maxresult, String lotno, String type) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.MAXRESULT, maxresult);
			jsonProtocol.put(ProtocolManager.PAGEINDEX, newPage);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, "selectAutoJoin");
			jsonProtocol.put(ProtocolManager.PHONE_NUM, phonenum);
			
			jsonProtocol.put(ProtocolManager.LOTNO, lotno);
			jsonProtocol.put("dateType", type);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

}
