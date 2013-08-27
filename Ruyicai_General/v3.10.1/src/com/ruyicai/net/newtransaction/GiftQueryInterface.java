package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.newtransaction.pojo.BetAndWinAndTrackAndGiftQueryPojo;
import com.ruyicai.util.ProtocolManager;

/**
 * 赠送查询接口
 * 
 * @author miao
 */
public class GiftQueryInterface {

	private static String COMMAND = "AllQuery";

	private static GiftQueryInterface instance = null;

	private GiftQueryInterface() {
	}

	public synchronized static GiftQueryInterface getInstance() {
		if (instance == null) {
			instance = new GiftQueryInterface();
		}
		return instance;
	}

	/**
	 * 赠送查询方法
	 * 
	 * @param giftQueryPojo
	 * @return error_code | message<br>
	 *         000000 | 成功<br>
	 *         000047 | 无记录、<br>
	 *         999999 | 操作失败<br>
	 *         gift_phonenum 赠送人手机号<br>
	 *         gifted_phonenum 被赠送人手机号码<br>
	 *         betTime 赠送时间<br>
	 *         lotno 彩种<br>
	 *         batchcode 期号<br>
	 *         lotmulti 倍数<br>
	 *         amount 金额 金额单位为分<br>
	 *         bet_code 注码 <br>
	 */
	public String giftQuery(BetAndWinAndTrackAndGiftQueryPojo giftQueryPojo) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.USERNO, giftQueryPojo.getUserno());
			jsonProtocol.put(ProtocolManager.SESSIONID,
					giftQueryPojo.getSessionid());
			jsonProtocol.put(ProtocolManager.MAXRESULT,
					giftQueryPojo.getMaxresult());
			jsonProtocol.put(ProtocolManager.PAGEINDEX,
					giftQueryPojo.getPageindex());
			jsonProtocol.put(ProtocolManager.LOTNO, giftQueryPojo.getLotno());
			jsonProtocol.put(ProtocolManager.TYPE, giftQueryPojo.getType());
			jsonProtocol.put(ProtocolManager.BATCHCODE,
					giftQueryPojo.getBatchcode());
			jsonProtocol.put(ProtocolManager.PHONE_NUM,
					giftQueryPojo.getPhonenum());

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
}
