package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.newtransaction.pojo.ChangeWithdrawPojo;
import com.ruyicai.util.ProtocolManager;

public class QueryMoneyInterface {
	private static String COMMAND = "getCash";

	private QueryMoneyInterface() {
	}

	private static QueryMoneyInterface instance = null;

	public synchronized static QueryMoneyInterface getInstance() {
		if (instance == null) {
			instance = new QueryMoneyInterface();
		}
		return instance;
	}

	/**
	 * 查询提现记录
	 * 
	 */
	public static String QueryCash(String userno, String phonenum,
			String newPage, String maxresult) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.CASHTYPE, "cashRecord");
			jsonProtocol.put(ProtocolManager.PHONE_NUM, phonenum);
			jsonProtocol.put(ProtocolManager.PAGEINDEX, newPage);
			jsonProtocol.put(ProtocolManager.MAXRESULT, maxresult);
			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

}
