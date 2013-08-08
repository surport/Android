package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.activity.join.JoinDingActivity.CustomizeInfo;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;
import com.ruyicai.util.PublicMethod;

/**
 * 定制跟单联网
 * 
 * @author win
 * 
 */
public class CustomizeInterface {
	private static String COMMAND = "autoJoin";

	private static CustomizeInterface instance = null;

	private CustomizeInterface() {
	}

	public synchronized static CustomizeInterface getInstance() {
		if (instance == null) {
			instance = new CustomizeInterface();
		}
		return instance;
	}

	/**
	 * 充值的方法
	 */
	public static String customizeNet(CustomizeInfo info) {
		String re = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, "createAutoJoin");
			jsonProtocol.put(ProtocolManager.USERNO, info.getUserno());
			jsonProtocol.put(ProtocolManager.STARTER_USERNO,
					info.getStarterUserNo());
			jsonProtocol.put(ProtocolManager.TIMES, info.getTimes());
			jsonProtocol.put(ProtocolManager.JOIN_AMT,
					PublicMethod.toFen(info.getJoinAmt()));
			jsonProtocol.put(ProtocolManager.SAFE_AMT,
					PublicMethod.toFen(info.getSafeAmt()));
			jsonProtocol.put(ProtocolManager.JOIN_TYPE, info.getJoinType());
			jsonProtocol.put(ProtocolManager.MAX_AMT,
					PublicMethod.toFen(info.getMaxAmt()));
			jsonProtocol.put(ProtocolManager.LOTNO, info.getLotno());
			jsonProtocol.put(ProtocolManager.PERCENT, info.getPercent());
			jsonProtocol.put(ProtocolManager.FORCE_JOIN, info.getForceJoin());
			re = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return re;
	}
}
