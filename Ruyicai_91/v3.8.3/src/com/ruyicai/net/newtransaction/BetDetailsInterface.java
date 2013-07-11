package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.newtransaction.pojo.BetAndGiftPojo;
import com.ruyicai.util.ProtocolManager;

public class BetDetailsInterface {
	private static String COMMAND = "select";
	private static BetDetailsInterface instance = null;

	private BetDetailsInterface() {
	}

	public static synchronized BetDetailsInterface getInstance() {
		if (instance == null) {
			instance = new BetDetailsInterface();
		}
		return instance;
	}

	/**
	 * 购彩和赠送彩票的方法
	 * 
	 * @param userno
	 *            用户编号 用户注册成功返回的用户编号
	 * @param batchcode
	 *            投注期号 用户投注的当前期（可以空）
	 * @param batchnum
	 *            追号期数 默认为1（不追号）
	 * @param bet_code
	 *            注码
	 * @param lotno
	 *            彩种编号 投注彩种，如：双色球为F47104
	 * @param lotmulti
	 *            倍数 投注的倍数
	 * @param sellway
	 *            销售方式 (可以空)
	 * @param amount
	 *            金额 单位为分（总金额）
	 * @param bettype
	 *            投注类型 用于区分购彩还是赠彩 投注为bet,赠彩为gift
	 * @param sessionid
	 *            服务器返回的sessionid
	 * @param to_mobile_code
	 *            用户赠送的手机号(可以空)
	 * @param advice
	 *            用户赠送彩票是的赠语（可以空）
	 * @param infoway
	 *            通过咨询投注(可以空)
	 * @param command
	 * @return 返回方式{error_code，message}信息 error_code = 000000,message = 投注成功;
	 *         error_code = 070002,message = 未登录; error_code = 040006,message =
	 *         余额不足; error_code = 999999,message = 操作失败;
	 */
	public String betDetails(String id) {
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, "betCodeAnalysis");
			jsonProtocol.put(ProtocolManager.MODIFY_ID, id);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
}
