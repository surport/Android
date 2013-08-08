package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.newtransaction.pojo.UserScroeDetailQueryPojo;
import com.ruyicai.util.ProtocolManager;

public class SetupnicknameInterface {
	private static String COMMAND = "updateUserInfo";

	private static SetupnicknameInterface instance = null;

	private SetupnicknameInterface() {
	}

	public synchronized static SetupnicknameInterface getInstance() {
		if (instance == null) {
			instance = new SetupnicknameInterface();
		}
		return instance;
	}

	/**
	 * 积分明细
	 * 
	 * @param userno
	 *            用户编号 用户注册成功返回的用户编号<br>
	 * @param startline
	 *            开始条数<br>
	 * @param stopline
	 *            截止条数<br>
	 * @param pageindex
	 *            第几页，比如第二页,pageindex=2<br>
	 * @param sessionid
	 * <br>
	 * 
	 * @return error_code | message <br>
	 *         000000 |成功<br>
	 *         000047 |无记录<br>
	 *         999999 |操作失败<br>
	 *         type_memo : 交易类型<br>
	 *         amount :变动金额 金额单位为分<br>
	 *         plattime : 交易时间 <br>
	 */
	public String setupnickname(String userno, String nickname) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.TYPE, "updateNickName");
			// jsonProtocol.put(ProtocolManager.PHONE_NUM, phonenum);
			jsonProtocol.put("nickName", nickname);

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

}
