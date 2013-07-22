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
public class LoginAcrossWeibo {
	private static String COMMAND = "login";
	private static LoginAcrossWeibo instance = null;

	private LoginAcrossWeibo() {

	}

	public static synchronized LoginAcrossWeibo getInstance() {
		if (instance == null) {
			instance = new LoginAcrossWeibo();
		}
		return instance;
	}

	/**
	 * 客户端第三方登录方法
	 * 
	 * @param source
	 *            第三方标识
	 * @param openId
	 *            第三方UID
	 * @param nickName
	 *            昵称
	 * @return error_code | message<br>
	 *         000000 | 成功<br>
	 *         999999 | 操作失败<br>
	 *         sessionid : 后台返回的sessionid <br>
	 *         userno : 后台返回的用户编号
	 */
	public static String login(String source, String openId, String nickName,String alias) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, "unionLogin");
			jsonProtocol.put("source", source);
			jsonProtocol.put("openId", openId);
			jsonProtocol.put("nickName", nickName);
			jsonProtocol.put(ProtocolManager.ALIAS, alias);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

}
