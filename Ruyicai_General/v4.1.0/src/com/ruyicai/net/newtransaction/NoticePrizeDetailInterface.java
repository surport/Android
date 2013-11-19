package com.ruyicai.net.newtransaction;

import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * 开奖明细查询接口
 * 
 * @author miao
 * 
 */
public class NoticePrizeDetailInterface {
	private static final String COMMAND = "AllQuery";
	private static NoticePrizeDetailInterface prizedetail = null;

	private NoticePrizeDetailInterface() {

	}

	public synchronized static NoticePrizeDetailInterface getInstance() {
		if (prizedetail == null) {
			prizedetail = new NoticePrizeDetailInterface();
		}
		return prizedetail;
	}

	/**
	 * 根据彩种和彩种的期号来获取本期号对应的开奖详情
	 * 
	 * @param lotno
	 *            彩种编号
	 * @param batchcode
	 *            彩种的期号
	 * @return 返回一个还有开奖详情相关数据的JSONObject对象
	 */
	public JSONObject getNoticePrizeDetail(String lotno, String batchcode) {
		String result = "";
		try {
			JSONObject jsonProtocol = ProtocolManager.getInstance()
					.getDefaultJsonProtocol();
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.TYPE, "winInfoDetail");
			jsonProtocol.put(ProtocolManager.LOTNO, lotno);
			jsonProtocol.put(ProtocolManager.BATCHCODE, batchcode);
			result = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
			return new JSONObject(result);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return null;
	}

}
