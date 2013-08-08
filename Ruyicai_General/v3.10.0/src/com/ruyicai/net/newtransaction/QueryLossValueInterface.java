package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * 遗漏值查询
 * 
 * @author PengCX
 * 
 */
public class QueryLossValueInterface {
	private static String COMMAND = "missValue";
	private static String REGUESTTYPE = "list";

	private static QueryLossValueInterface instance = null;

	private QueryLossValueInterface() {

	}

	public synchronized static QueryLossValueInterface getInstance() {
		if (instance == null) {
			instance = new QueryLossValueInterface();
		}

		return instance;
	}

	public JSONObject lossValueQuery(String sellWay, String lotNo,
			String batchNum) {
		String result = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();

		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, REGUESTTYPE);
			jsonProtocol.put(ProtocolManager.SELLWAY, sellWay);
			jsonProtocol.put(ProtocolManager.LOTNO, lotNo);
			jsonProtocol.put(ProtocolManager.BATCHNUM, batchNum);

			result = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
			return new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}
}
