package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.AgentNumConstants;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * 用户注册接口调用
 * 
 * @author haojie
 * 
 */
public class RegisterInterface {

	private static RegisterInterface instance = null;
	private static String COMMAND = "register";

	private RegisterInterface() {

	}

	public static synchronized RegisterInterface getInstance() {
		if (instance == null) {
			instance = new RegisterInterface();
		}
		return instance;
	}

	String error_code = "00";

	public String userregister(String login_User, String login_password,
			String login_Id, String realname, String referrer) {

		JSONObject obj = null;
		// String re = register(login_User, login_password, login_Id);

		String re = registerLotserver(login_User, login_password, login_Id,
				realname, referrer);

		return re;

	}

	/**
	 * 用户注册的调用方法
	 * 
	 * @param mobileCode
	 *            用户的手机号
	 * @param password
	 *            用户的密码
	 * @param certid
	 *            用户的身份证号
	 * @param realname
	 *            用户的真实姓名
	 * @return error_code | message <br>
	 *         000000 | 成功,<br>
	 *         999999 | 操作失败 <br>
	 */

	public String registerLotserver(String mobileCode, String password,
			String certid, String realname, String referrer) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.PHONE_NUM, mobileCode);
			jsonProtocol.put(ProtocolManager.PASSWORD, password);
			jsonProtocol.put(ProtocolManager.CERTID, certid);
			jsonProtocol.put(ProtocolManager.NAME, realname);
			jsonProtocol.put(ProtocolManager.AGENCYNO,
					AgentNumConstants.AGENT_NUM);
			jsonProtocol.put(ProtocolManager.RECOMMENDER, referrer);
			// jsonProtocol.put(ProtocolManager.ISBINDPHONE, isBindPhone);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

}
