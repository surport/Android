package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.newtransaction.pojo.BetAndWinAndTrackAndGiftQueryPojo;
import com.ruyicai.util.ProtocolManager;

/**
 * 客户端投注查询接口
 * 
 * @author miao
 * 
 */
public class BetQueryInterface {
	private static String COMMAND = "select";

	private static BetQueryInterface instance = null;

	private BetQueryInterface() {
	}

	public synchronized static BetQueryInterface getInstance() {
		if (instance == null) {
			instance = new BetQueryInterface();
		}
		return instance;
	}

	/**
	 * 投注查询的方法
	 * 
	 * @param betQueryPojo
	 *            下面是 betQueryPojo中的变量
	 * @param userno
	 *            用户编号 登录成功返回的用户编号如00000001<br>
	 * @param startline
	 *            开始条数 开始条数<br>
	 * @param stopline
	 *            截止条数 截止条数<br>
	 * @param lotno
	 *            (预留的可以传空)彩种 彩种（可以传空预留）<br>
	 * @param sessionid
	 *            sessionid<br>
	 * @param batchcode
	 *            期号 期号<br>
	 * @param type
	 *            查询类型 type 投注查询为bet<br>
	 * @param command
	 *            请求 command:QueryLot<br>
	 * @return error_code|message<br>
	 *         000000 |成功<br>
	 *         000047 |无记录<br>
	 *         999999 |操作失败<br>
	 *         bettime ：投注时间 <br>
	 *         lotno ：彩种 <br>
	 *         batchcode : 期号<br>
	 *         lotmulti :倍数<br>
	 *         amount :金额 金额单位为分<br>
	 *         bet_code : 注码 <br>
	 */
	public String betQuery(BetAndWinAndTrackAndGiftQueryPojo betQueryPojo) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();

		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.USERNO, betQueryPojo.getUserno());
			jsonProtocol.put(ProtocolManager.MAXRESULT,
					betQueryPojo.getMaxresult());
			jsonProtocol.put(ProtocolManager.PAGEINDEX,
					betQueryPojo.getPageindex());
			jsonProtocol.put(ProtocolManager.LOTNO, betQueryPojo.getLotno());
			jsonProtocol.put(ProtocolManager.REQUESTTYPE,
					betQueryPojo.getType());
			jsonProtocol.put("state",
					betQueryPojo.getState());
			jsonProtocol.put("dateType",
					betQueryPojo.getDateType());
			
			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}

}
