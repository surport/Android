package com.ruyicai.net.newtransaction;

import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * 开奖查询获取更多接口
 * 
 * @author miao
 * 
 */
public class PrizeInfoInterface {
	private static final String COMMAND = "QueryLot";
	private static PrizeInfoInterface prize = null;

	private PrizeInfoInterface() {

	}

	public synchronized static PrizeInfoInterface getInstance() {
		if (prize == null) {
			prize = new PrizeInfoInterface();
		}
		return prize;
	}

	/**
	 * 根据彩种和彩种的期号来获取本期号对应的开奖详情
	 * 
	 * @param lotno
	 *            彩种编号
	 * @param pageindex
	 *            当前页
	 * @param maxresult
	 *            当前页返回的最大条数
	 * @return 返回一个还有开奖详情相关数据的JSONObject对象
	 */
	public JSONObject getNoticePrizeInfo(String lotno, String pageindex,
			String maxresult) {
		String result = "";
		try {
			JSONObject jsonProtocol = ProtocolManager.getInstance()
					.getDefaultJsonProtocol();
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.TYPE, "winInfoList");
			jsonProtocol.put(ProtocolManager.LOTNO, lotno);
			jsonProtocol.put(ProtocolManager.PAGEINDEX, pageindex);
			jsonProtocol.put(ProtocolManager.MAXRESULT, maxresult);
			result = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
			return new JSONObject(result);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}

}
