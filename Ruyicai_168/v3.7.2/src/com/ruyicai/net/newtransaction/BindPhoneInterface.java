package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * 绑定手机号中提交验证码的接口
 * 
 * @author miao
 * @time 2012-2-13 2:00pm
 */
public class BindPhoneInterface {

	private static String COMMAND = "updateUserInfo";

	private static BindPhoneInterface instance = null;

	private BindPhoneInterface() {

	}

	public synchronized static BindPhoneInterface getInstance() {
		if (instance == null) {
			instance = new BindPhoneInterface();
		}
		return instance;
	}

	/**
	 * 提交要绑定的手机号码
	 * 
	 * @param bindPhoneNum
	 *            用户要绑定的手机号
	 * @return error_code | message <br>
	 *         000000 | 成功<br>
	 *         000005 | 失败 <br>
	 *         999999 | 操作失败
	 */
	public String submitPhonenum(String bindPhoneNum, String phonenum,
			String userno) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.PHONE_NUM, phonenum);
			jsonProtocol.put(ProtocolManager.BINDPHONENUM, bindPhoneNum);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.TYPE, "bindPhoneSecurityCode");

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 提交发送到你手机上的验证码
	 * 
	 * @param SecurityCode
	 * @param phonenum
	 *            用户手机号
	 * @param userno
	 *            用户的userno
	 * @param bindphonenum
	 * @return
	 */
	public String submitSecurityCode(String SecurityCode, String bindphonenum,
			String phonenum, String userno) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.PHONE_NUM, phonenum);
			jsonProtocol.put(ProtocolManager.SECURITYCODE, SecurityCode);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.BINDPHONENUM, bindphonenum);
			jsonProtocol.put(ProtocolManager.TYPE, "bindPhone");

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 接触绑定
	 * 
	 * @param phonenum
	 * @param userno
	 * @return
	 */
	public String unBind(String phonenum, String userno) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.PHONE_NUM, phonenum);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.TYPE, "removeBindPhone");

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
}
