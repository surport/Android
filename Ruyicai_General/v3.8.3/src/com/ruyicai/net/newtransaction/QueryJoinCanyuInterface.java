/**
 * 
 */
package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * @author Administrator
 * 
 */
public class QueryJoinCanyuInterface {
	private static String COMMAND = "QueryLot";
	private static QueryJoinCanyuInterface instance = null;

	private QueryJoinCanyuInterface() {
	}

	public synchronized static QueryJoinCanyuInterface getInstance() {
		if (instance == null) {
			instance = new QueryJoinCanyuInterface();
		}
		return instance;
	}

	/**
	 * 合买详情的方法
	 */
	public static String queryLotJoincanyu(String id, String userno,
			String phone, String pageIndex, String maxrsult) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.TYPE, "caseLotBuys");
			jsonProtocol.put(ProtocolManager.CASEID, id);
			jsonProtocol.put(ProtocolManager.PHONE_NUM, phone);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.PAGEINDEX, pageIndex);
			jsonProtocol.put(ProtocolManager.MAXRESULT, maxrsult);
			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
}
