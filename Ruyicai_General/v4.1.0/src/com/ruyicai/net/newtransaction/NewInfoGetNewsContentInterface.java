package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * 获取咨询内容的新接口
 * 
 * @author Administrator
 * 
 */
public class NewInfoGetNewsContentInterface {

	private static String COMMAND = "information";
	public static NewInfoGetNewsContentInterface instance = null;

	private NewInfoGetNewsContentInterface() {

	}

	public static NewInfoGetNewsContentInterface getInstance() {
		if (instance == null) {
			instance = new NewInfoGetNewsContentInterface();
		}
		return instance;
	}

	/**
	 * 获取新闻内容
	 * 
	 * @param newsId
	 *            新闻Id
	 * @param newsType
	 *            "content"
	 * @return
	 */
	public static String getNewsContent(String newsId) {
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.NEWSID, newsId);
			jsonProtocol.put(ProtocolManager.NEWSTYPE, "content");

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

}
