package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * 用户中心，余额查询
 * 
 * @author miao
 * 
 */
public class BalanceQueryInterface {

	private static String COMMAND = "AllQuery";
	private static BalanceQueryInterface instance = null;

	private BalanceQueryInterface() {
	}

	public synchronized static BalanceQueryInterface getInstance() {
		if (instance == null) {
			instance = new BalanceQueryInterface();
		}
		return instance;
	}

	/**
	 * 余额查询方法
	 * 
	 * @param userno
	 * @param sessionid
	 * @param phonenum
	 * @return error_code| message<br>
	 *         000000 | 成功<br>
	 *         000047 | 无记录<br>
	 *         999999 |操作失败<br>
	 *         balance :余额 用户余额<br>
	 *         drawbalance :可提现余额<br>
	 *         freezebalance: 用户冻结金额<br>
	 *         bet_balance : 投注金额<br>
	 */
	public static String balanceQuery(String userno, String sessionid,
			String phonenum) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.SESSIONID, sessionid);
			jsonProtocol.put(ProtocolManager.TYPE, "balance");
			jsonProtocol.put(ProtocolManager.PHONE_NUM, phonenum);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
}
