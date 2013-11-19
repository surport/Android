package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * <b>修改密码的接口类</b>
 * 
 * @author miao
 */
public class ChangePasswordInterface {

	private static String COMMAND = "updatePass";

	private static ChangePasswordInterface instance = null;

	private ChangePasswordInterface() {

	}

	public synchronized static ChangePasswordInterface getInstance() {
		if (instance == null) {
			instance = new ChangePasswordInterface();
		}
		return instance;
	}

	/**
	 * 修改密码
	 * 
	 * @param phonenum
	 *            用户注册手机号
	 * @param oldPass
	 *            用户原来的密码
	 * @param newPass
	 *            用户的新密码
	 * @return error_code | message <br>
	 *         000000 | 成功<br>
	 *         000005 | 失败 <br>
	 *         999999 | 操作失败
	 */
	public String changePass(String phonenum, String oldPass, String newPass,
			String sessionid, String userno) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.PHONE_NUM, phonenum);
			jsonProtocol.put(ProtocolManager.OLDPASS, oldPass);
			jsonProtocol.put(ProtocolManager.NEWPASS, newPass);
			jsonProtocol.put(ProtocolManager.SESSIONID, sessionid);
			jsonProtocol.put(ProtocolManager.USERNO, userno);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
}
