package com.ruyicai.net.newtransaction.recharge;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class AlipaySecurePayInterface {

	private static AlipaySecurePayInterface instance = null;

	private AlipaySecurePayInterface() {
	}

	public synchronized static AlipaySecurePayInterface getInstance() {
		if (instance == null) {
			instance = new AlipaySecurePayInterface();
		}
		return instance;
	}

	/**
	 * 支付宝安全支付
	 * 
	 * @param
	 * @return
	 */
	public String alipaySecurePay(String amount, String userno, String phonenum) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, "recharge");
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.PHONENUM, phonenum);
			jsonProtocol.put(ProtocolManager.CARDTYPE, "0300");
			jsonProtocol.put(ProtocolManager.RECHARGETYPE, "07");
			jsonProtocol.put(ProtocolManager.AMOUNT, amount);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

}
