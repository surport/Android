package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * 绑定邮箱接口
 * 
 * @author Administrator
 * 
 */
public class BindEmailInterface {
	private static String COMMAND = "userInfo";

	private static BindEmailInterface instance = null;

	private BindEmailInterface() {

	}

	public synchronized static BindEmailInterface getInstance() {
		if (instance == null) {
			instance = new BindEmailInterface();
		}
		return instance;
	}

	/**
	 * 绑定邮箱
	 * 
	 * @param userno
	 *            用户编号
	 * @param eamil
	 *            邮箱地址
	 * @return 返回json字符串
	 */
	public String bindEmail(String userno, String eamil) {
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();

		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, "bindEmail");
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.EMAIL, eamil);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * 解绑邮箱
	 * @param userno 用户编号
	 * @return 返回json字符串
	 */
	public String unBindEmail(String userno){
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, "removeBindEmail");
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			
			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return "";
		
	}
}
