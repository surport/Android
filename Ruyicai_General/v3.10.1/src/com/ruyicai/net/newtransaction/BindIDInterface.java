package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class BindIDInterface {

	private static String COMMAND = "updateUserInfo";

	private static BindIDInterface instance = null;

	private BindIDInterface() {

	}

	public synchronized static BindIDInterface getInstance() {
		if (instance == null) {
			instance = new BindIDInterface();
		}
		return instance;
	}

	/**
	 * 绑定身份方法
	 * 
	 * @param phonenum
	 *            用户注册手机号
	 * @param name
	 *            用户真实姓名
	 * @param certid
	 *            用户身份证号
	 * @param userno
	 *            userno
	 * @return error_code | message <br>
	 *         000000 | 成功<br>
	 *         000005 | 失败 <br>
	 *         999999 | 操作失败
	 */
	public String bindID(String phonenum, String name, String certid,
			String userno) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.NAME, name);
			jsonProtocol.put(ProtocolManager.PHONE_NUM, phonenum);
			jsonProtocol.put(ProtocolManager.CERTID, certid);
			jsonProtocol.put(ProtocolManager.USERNO, userno);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
}
