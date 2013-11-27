package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class CancleAutoLoginInterface {

	private static String COMMAND = "updateUserInfo";
	private static CancleAutoLoginInterface instance = null;

	private CancleAutoLoginInterface() {
	}

	public synchronized static CancleAutoLoginInterface getInstance() {
		if (instance == null) {
			instance = new CancleAutoLoginInterface();
		}
		return instance;
	}

	/**
	 * 取消提现调用的方法
	 * 
	 * @param userno
	 * @param sessionid
	 * @param phonenum
	 * @return error_code|message<br/>
	 *         000000| 成功<br/>
	 *         , 000045|提现记录已经取消<br/>
	 *         999999|操作失败
	 */
	public static String cancelAutoLogin(String userno) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.TYPE, "cancelAutoLogin");

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

	// {"command":"updateUserInfo",
	// "imei":"3434343434534534",
	// "imsi":"460023123123123",
	// "softwareversion":"2.3",
	// "smscenter":"+8613800100500",
	// "machineid":"HTC Desire",
	// "coopid":"001",
	// "platform":"android",
	// "userno":"00000001",
	// "type":"cancelAutoLogin"}

}
