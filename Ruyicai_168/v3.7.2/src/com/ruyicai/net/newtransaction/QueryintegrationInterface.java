package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * 查询DNA充值接口
 * 
 * @author miao
 * 
 */
public class QueryintegrationInterface {

	private static String COMMAND = "updateUserInfo";

	private static QueryintegrationInterface instance = null;

	private QueryintegrationInterface() {

	}

	public synchronized static QueryintegrationInterface getInstance() {
		if (instance == null) {
			instance = new QueryintegrationInterface();
		}
		return instance;

	}

	/**
	 * 查询ＤＮＡ充值的方法
	 * 
	 * @param phonenum
	 *            　公共常量：用户手机号
	 * @param sessionid
	 *            　公共常量：服务器返回的ｓｅｓｓｉｏｎｉｄ
	 * @param userno
	 *            　公共常量：用户的userno
	 * @return error_code
	 *         如果error_code为"000047"为message="无记录"；如果error_code为"070002",
	 *         message="未登录"
	 *         如果error_code为"999999",message="操作失败";如果error_code为"000000"
	 *         ,message="成功"
	 * **/
	public String queryintegration(String phonenum, String sessionid,
			String userno) {
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.TYPE, "userCenter");
			jsonProtocol.put(ProtocolManager.SESSIONID, sessionid);
			jsonProtocol.put(ProtocolManager.PHONE_NUM, phonenum);
			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";

	}

}
