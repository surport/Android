/**
 * 
 */
package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * @author Administrator
 * 
 */
public class QueryJoinInfoInterface {
	private static String COMMAND = "QueryLot";
	public static final String MINAMT = "buyAmt";
	public static final String PROGRESS = "progress";
	public static final String POPULARITY = "participantCount";

	public static final String TOTALAMT = "totalAmt";
	public static final String DESC = "desc";
	public static final String ASC = "asc";
	private static QueryJoinInfoInterface instance = null;

	private QueryJoinInfoInterface() {
	}

	public synchronized static QueryJoinInfoInterface getInstance() {
		if (instance == null) {
			instance = new QueryJoinInfoInterface();
		}
		return instance;
	}

	/**
	 * 查询参与合买的方法
	 * 
	 * 按单价排序orderBy=minAmt 按进度排序orderBy=progress 按总额排序orderBy=totalAmt
	 * orderDir=desc//降序 orderDir=asc//升序
	 */
	public static String queryLotJoinInfo(String lotno, String batchcode,
			String orderBy, String orderDir, String newPage, String maxresult) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.LOTNO, lotno);
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.MAXRESULT, maxresult);
			jsonProtocol.put(ProtocolManager.PAGEINDEX, newPage);
			jsonProtocol.put(ProtocolManager.TYPE, "querycaselot");
			jsonProtocol.put(ProtocolManager.ORDERBY, orderBy);
			jsonProtocol.put(ProtocolManager.ORDERDIR, orderDir);
			jsonProtocol.put(ProtocolManager.BATCHCODE, batchcode);
			jsonProtocol.put(ProtocolManager.STATE, "");
			Log.v("bet", jsonProtocol.toString());

			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
	
	
	/**
	 * 查询参与合买的方法
	 * 
	 * 按单价排序orderBy=minAmt 按进度排序orderBy=progress 按总额排序orderBy=totalAmt
	 * orderDir=desc//降序 orderDir=asc//升序
	 */
	public static String queryLotJoinInfo(String lotno, String batchcode,
			String orderBy, String orderDir, String newPage, String maxresult,String name) {

		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.LOTNO, lotno);
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.MAXRESULT, maxresult);
			jsonProtocol.put(ProtocolManager.PAGEINDEX, newPage);
			jsonProtocol.put(ProtocolManager.TYPE, "querycaselot");
			jsonProtocol.put(ProtocolManager.ORDERBY, orderBy);
			jsonProtocol.put(ProtocolManager.ORDERDIR, orderDir);
			jsonProtocol.put(ProtocolManager.BATCHCODE, batchcode);
			jsonProtocol.put(ProtocolManager.INFO, name);
			jsonProtocol.put(ProtocolManager.STATE, "");
			Log.v("bet", jsonProtocol.toString());
			Log.v("bet", jsonProtocol.toString());
			return InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return "";
	}
	
}
