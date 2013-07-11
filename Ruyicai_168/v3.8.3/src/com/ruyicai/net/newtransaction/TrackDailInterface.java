package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * 取消追号接口
 * 
 * @author miao
 * 
 */
public class TrackDailInterface {

	private static String COMMAND = "AllQuery";

	private TrackDailInterface() {

	}

	private static TrackDailInterface instance = null;

	public synchronized static TrackDailInterface getInstance() {
		if (instance == null) {
			instance = new TrackDailInterface();
		}
		return instance;
	}

	/**
	 * 取消追号的方法
	 * 
	 * @param userno
	 *            用户编号，用户注册成功返回给用户的编号
	 * @param sessionid
	 *            sessionid
	 * @param command
	 *            此处为 “cancelTrack”
	 * @param tsubscribeid
	 *            追号记录id
	 * @param phonenum
	 *            手机号
	 * @return {error_code,message} 000000:取消成功 070002:未登录 其他：其它的代表失败
	 */
	public String looktrack(String tsubscribeid) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		// Log.v("getDefaultJsonProtocol",jsonProtocol+"");
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.TYPE, "trackDetail");
			jsonProtocol.put(ProtocolManager.TSUSBSCRIBEID, tsubscribeid);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

}
