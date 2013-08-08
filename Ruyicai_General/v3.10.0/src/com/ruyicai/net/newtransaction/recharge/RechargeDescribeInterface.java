package com.ruyicai.net.newtransaction.recharge;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class RechargeDescribeInterface {

	private static RechargeDescribeInterface instance = null;

	RechargeDescribeInterface() {
	}

	public synchronized static RechargeDescribeInterface getInstance() {
		if (instance == null) {
			instance = new RechargeDescribeInterface();
		}
		return instance;
	}

	/**
	 * 充值描述接口
	 * 
	 * @param
	 * @return
	 */
	public JSONObject rechargeDescribe(String keyStr) {
		String result = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, "information");
			jsonProtocol.put("newsType", "messageContent");
			jsonProtocol.put("keyStr", keyStr);

			result = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
			return new JSONObject(result);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 账户充值显示状态
	 * @param
	 * @return
	 */
	public JSONObject rechargeShowState() {
		String result = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, "select");
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, "chargeCenter");

			result = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
			return new JSONObject(result);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
