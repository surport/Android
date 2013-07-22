package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.newtransaction.pojo.BetAndWinAndTrackAndGiftQueryPojo;
import com.ruyicai.util.ProtocolManager;

/**
 * 中奖查询接口
 * 
 * @author miao
 * 
 */
public class WinQueryInterface {

	private static String COMMAND = "select";

	private WinQueryInterface() {
	}

	private static WinQueryInterface instance = null;

	public synchronized static WinQueryInterface getInstance() {
		if (instance == null) {
			instance = new WinQueryInterface();
		}
		return instance;
	}

	/**
	 * 中奖查询的方法
	 * 
	 * @param winQueryPojo
	 * <br>
	 *            startline 开始条数 开始条数<br>
	 *            stopline 截止条数 截止条数<br>
	 *            lotno 彩种 彩种（可以传空预留）<br>
	 *            batchcode 期号 期号<br>
	 * @return error_code |message <br>
	 *         000000 |成功<br>
	 *         000047 |无记录<br>
	 *         bettime ：投注时间<br>
	 *         lotno :彩种 <br>
	 *         batchcode :期号<br>
	 *         lotmulti :倍数<br>
	 *         amount :金额 返回xxx元<br>
	 *         bet_code :注码
	 */
	public String winQuery(BetAndWinAndTrackAndGiftQueryPojo winQueryPojo) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.USERNO, winQueryPojo.getUserno());
			jsonProtocol.put(ProtocolManager.MAXRESULT,
					winQueryPojo.getMaxresult());
			jsonProtocol.put(ProtocolManager.PAGEINDEX,
					winQueryPojo.getPageindex());
			jsonProtocol.put(ProtocolManager.REQUESTTYPE,
					winQueryPojo.getType());

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

}
