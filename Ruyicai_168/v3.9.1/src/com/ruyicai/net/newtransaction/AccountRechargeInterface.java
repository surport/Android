package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.newtransaction.pojo.RechargePojo;
import com.ruyicai.util.ProtocolManager;

/**
 * 银行充值获取银行名
 * 
 * @author Administrator
 * 
 */
public class AccountRechargeInterface {
	private static String COMMAND = "recharge";

	private static AccountRechargeInterface instance = null;

	private AccountRechargeInterface() {
	}

	public synchronized static AccountRechargeInterface getInstance() {
		if (instance == null) {
			instance = new AccountRechargeInterface();
		}
		return instance;
	}

	/**
	 * 充值的方法
	 */
	public static String recharge(String userno, String phone, String type) {
		String re = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.RECHARGETYPE, type);
			jsonProtocol.put(ProtocolManager.PHONE_NUM, phone);
			re = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}

}
