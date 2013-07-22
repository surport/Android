package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * 
 * @author haojie 开奖信息联网数据接口
 */
public class NoticeInterface {

	private static NoticeInterface noticeInterface = null;

	private static String COMMAND = "lotteryinfomation";

	private NoticeInterface() {

	}

	public synchronized static NoticeInterface getInstance() {
		if (noticeInterface == null) {
			noticeInterface = new NoticeInterface();
		}
		return noticeInterface;
	}

	/**
	 * 获取所有的开奖信息
	 * 
	 * @return
	 */
	public JSONObject getLotteryAllNotice() {
		String result = "";
		try {
			JSONObject jsonProtocol = ProtocolManager.getInstance()
					.getDefaultJsonProtocol();
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			result = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
			return new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
			// 联网失败了
		}

		return null;
	}

}
