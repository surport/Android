package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class QueryLatelyWithdrawInterface {

	private static String COMMAND = "getCash";
	private static QueryLatelyWithdrawInterface instance = null;

	private QueryLatelyWithdrawInterface() {
	}

	public synchronized static QueryLatelyWithdrawInterface getInstance() {
		if (instance == null) {
			instance = new QueryLatelyWithdrawInterface();
		}
		return instance;
	}

	/**
	 * 查询用户最近提现的方法
	 * 
	 * @param userno
	 *            用户编号
	 * @param sessionid
	 *            sessionid
	 * @param phonenum
	 *            用户注册手机号
	 * @return error_code|message<br/>
	 *         000000 | 成功<br/>
	 *         000047 |无提现记录<br>
	 *         999999 |操作失败<br>
	 *         cashdetailid ：提现记录id<br>
	 *         amount ：金额<br>
	 *         araeaname ：地址<br>
	 *         bankname ：银行名称<br>
	 *         stat ：处理状态（1待审核,2审核中，3成功，4驳回取消）<br>
	 *         allbankname ：所有可以提现的银行名称<br>
	 */
	public static String queryLatelyWithdraw(String userno, String sessionid,
			String phonenum) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.SESSIONID, sessionid);
			jsonProtocol.put(ProtocolManager.CASHTYPE, "queryCash");
			jsonProtocol.put(ProtocolManager.PHONE_NUM, phonenum);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
}
