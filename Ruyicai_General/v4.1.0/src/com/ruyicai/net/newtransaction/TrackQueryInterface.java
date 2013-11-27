package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.newtransaction.pojo.BetAndWinAndTrackAndGiftQueryPojo;
import com.ruyicai.util.ProtocolManager;

/**
 * 客户端追号查询接口
 * 
 * @author miao
 * 
 */
public class TrackQueryInterface {

	private static String COMMAND = "AllQuery";

	private TrackQueryInterface() {
	}

	private static TrackQueryInterface instance = null;

	public synchronized static TrackQueryInterface getInstance() {
		if (instance == null) {
			instance = new TrackQueryInterface();
		}
		return instance;
	}

	/**
	 * 追号查询方法
	 * 
	 * @param trackQueryPojo
	 * @return error_code | message <br>
	 *         000000 |成功<br>
	 *         000047 |无记录<br>
	 *         999999 |操作失败<br>
	 *         bettime : 投注时间 投注时间<br>
	 *         lotno : 彩种<br>
	 *         batchcode : 期号<br>
	 *         lotmulti : 倍数<br>
	 *         amount : 追号总金额 金额单位为分<br>
	 *         stat : 追号状态（0 进行中 2 已取消 3 已追完）<br>
	 *         beginbatch ： 开始期 追号的开始期<br>
	 *         lastbatch ： 追号的截止期<br>
	 *         batchnum ： 追号期数 <br>
	 *         betnum ： 注数 <br>
	 *         lastnum ： 已经购买的期数 <br>
	 */
	public String trackQuery(BetAndWinAndTrackAndGiftQueryPojo trackQueryPojo) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol
					.put(ProtocolManager.USERNO, trackQueryPojo.getUserno());
			jsonProtocol.put(ProtocolManager.SESSIONID,
					trackQueryPojo.getSessionid());
			jsonProtocol.put(ProtocolManager.BATCHCODE,
					trackQueryPojo.getBatchcode());
			jsonProtocol.put(ProtocolManager.PAGEINDEX,
					trackQueryPojo.getPageindex());
			jsonProtocol.put(ProtocolManager.MAXRESULT,
					trackQueryPojo.getMaxresult());
			jsonProtocol.put(ProtocolManager.TYPE, trackQueryPojo.getType());
			jsonProtocol.put(ProtocolManager.LOTNO, trackQueryPojo.getLotno());
			jsonProtocol.put(ProtocolManager.PHONE_NUM,
					trackQueryPojo.getPhonenum());
			
			jsonProtocol.put("state",
					trackQueryPojo.getState());
			jsonProtocol.put("dateType",
					trackQueryPojo.getDateType());

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";

	}

}
