package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * <b>注册接口</b>
 * 
 * @author miao
 */
public class LoginInterface {
	private static String COMMAND = "login";
	private static LoginInterface instance = null;

	private LoginInterface() {

	}

	public static synchronized LoginInterface getInstance() {
		if (instance == null) {
			instance = new LoginInterface();
		}
		return instance;
	}

	/**
	 * 客户端登录方法
	 * 
	 * @param phonenum
	 *            手机号
	 * @param password
	 *            密码
	 * @return error_code | message<br>
	 *         000000 | 成功<br>
	 *         999999 | 操作失败<br>
	 *         sessionid : 后台返回的sessionid <br>
	 *         userno : 后台返回的用户编号
	 */
	public static String login(String phonenum, String password,
			String isAutoLogin,String alias) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.PHONE_NUM, phonenum);
			jsonProtocol.put(ProtocolManager.PASSWORD, password);
			jsonProtocol.put(ProtocolManager.ISAUTOLOGIN, isAutoLogin);
			jsonProtocol.put(ProtocolManager.ALIAS, alias);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

}
