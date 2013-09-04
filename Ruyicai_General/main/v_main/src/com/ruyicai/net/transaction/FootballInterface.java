package com.ruyicai.net.transaction;

import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;
import com.ruyicai.util.URLEncoder;

public class FootballInterface {

	private static FootballInterface instance;

	public static synchronized FootballInterface getInstance() {
		if (instance == null) {
			instance = new FootballInterface();
		}
		return instance;
	}
	
	public String getAdvanceBatchCodeList(String Lotno) {
		String reValue = "";
		try {

			JSONObject jsonProtocol = ProtocolManager.getInstance()
					.getDefaultJsonProtocol();
			jsonProtocol.put(ProtocolManager.COMMAND, "QueryLot");
			jsonProtocol.put(ProtocolManager.TYPE, "zcIssue");
			jsonProtocol.put(ProtocolManager.LOTNO, Lotno);
			reValue = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reValue;
	}
	
	
	/**
	 * 获得足彩对阵
	 * 改为新接口 by yejc 20130829
	 */
	public String getZCData(String lotno, String batchCode) {
		String reValue = "";
		try {
			JSONObject jsonProtocol = ProtocolManager.getInstance()
					.getDefaultJsonProtocol();
			jsonProtocol.put(ProtocolManager.COMMAND, "zuCai");
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, "duiZhen"); //requestType":"duiZhen"
			jsonProtocol.put(ProtocolManager.LOTNO, lotno);
			jsonProtocol.put(ProtocolManager.BATCHCODE, batchCode);
			reValue = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reValue;
	}

	/**
	 * 获取足彩的分析信息
	 * 
	 * @param lotno
	 *            彩种
	 * @param batchCode
	 *            期号
	 * @param num
	 *            对应队列的num
	 * @return
	 */
	public String getZCInfo(String lotno, String batchCode, String num) {
		String action = "zcAction.do";
		String method = "getFlDataInfo";
		String reValue = "";
		try {
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("lotno", lotno);
			paras.put("batchCode", batchCode);
			paras.put("num", num);
			para = URLEncoder.encode(paras.toString());
			String re = InternetUtils
					.GetMethodOpenHttpConnectJrt(Constants.SERVER_URL + action
							+ ";jsessionid=" + Constants.sessionId + "?method="
							+ method + "&jsonString=" + para);
			if (re != null && re.length() > 0) {
				reValue = re;
			}
		} catch (Exception e) {
		}

		return reValue;
	}
}
