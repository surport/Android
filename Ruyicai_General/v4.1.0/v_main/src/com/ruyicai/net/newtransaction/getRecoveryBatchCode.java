package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * 取消追号接口
 * 
 * @author miao
 * 
 */
public class getRecoveryBatchCode {

	private static String COMMAND = "QueryLot";

	private getRecoveryBatchCode() {

	}

	private static getRecoveryBatchCode instance = null;

	public synchronized static getRecoveryBatchCode getInstance() {
		if (instance == null) {
			instance = new getRecoveryBatchCode();
		}
		return instance;
	}

	/**
	 
	 */
	public String getCode(String lotno, String batchnum) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		// Log.v("getDefaultJsonProtocol",jsonProtocol+"");
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.TYPE, "afterIssue");
			jsonProtocol.put(ProtocolManager.LOTNO, lotno);
			jsonProtocol.put(ProtocolManager.BATCHNUM, batchnum);
			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

}
