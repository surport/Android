package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * 北京的单场接口
 * 
 * @author Administrator
 * 
 */
public class BeiJingSingleGameInterface {
	private static String COMMAND = "beiDan";
	private static BeiJingSingleGameInterface instance = null;

	private BeiJingSingleGameInterface() {
	}

	public static synchronized BeiJingSingleGameInterface getInstance() {
		if (instance == null) {
			instance = new BeiJingSingleGameInterface();
		}
		return instance;
	}

	/**
	 * 根据彩种和期号获取对阵信息
	 * 
	 * @param lotno
	 *            彩种编号
	 * @param batchCode
	 *            当前期号
	 * @return 对阵Json字符串
	 */
	public String getAgainstInformations(String lotno, String batchCode) {
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();

		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, "duiZhen");
			jsonProtocol.put(ProtocolManager.LOTNO, lotno);
			jsonProtocol.put(ProtocolManager.BATCHCODE, batchCode);
			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
}
