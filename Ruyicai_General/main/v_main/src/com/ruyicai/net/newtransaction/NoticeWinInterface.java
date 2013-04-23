package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class NoticeWinInterface {
	private static NoticeWinInterface noticeInterface = null;

	private static String COMMAND = "QueryLot";

	private NoticeWinInterface() {

	}

	public synchronized static NoticeWinInterface getInstance() {
		if (noticeInterface == null) {
			noticeInterface = new NoticeWinInterface();
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
			jsonProtocol.put(ProtocolManager.TYPE, "winInfo");
			result = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
			Log.e("NoticeWinInterface", result + "");
			return new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
			// 联网失败了
		}

		return null;
	}
}
