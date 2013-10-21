package com.ruyicai.net.newtransaction;

import org.json.JSONObject;
import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/***
 * 
 * @author yejc
 *
 */
public class RuyiGuessInterface {
	private String COMMAND = "quiz";
	private String REQUESTTYPE = "list";
	private String DETAIL_REQUESTTYPE = "detail";
	private String SEND_REQUESTTYPE = "participate";
	
	private static RuyiGuessInterface instance;

	private RuyiGuessInterface() {}

	public synchronized static RuyiGuessInterface getInstance() {
		if (instance == null) {
			instance = new RuyiGuessInterface();
		}
		return instance;
	}
	
	/***
	 * 获取如意竞猜题目列表
	 * @param userno
	 * @param pageIndex
	 * @param maxResult
	 * @return
	 */
	public String getRuyiGuessList(String pageIndex, String maxResult, 
			String userno, String type) {
		String result = "";
		try {
			JSONObject jsonProtocol = ProtocolManager.getInstance()
					.getDefaultJsonProtocol();
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, REQUESTTYPE);
			jsonProtocol.put(ProtocolManager.PAGEINDEX, pageIndex);
			jsonProtocol.put(ProtocolManager.MAXRESULT, maxResult);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put(ProtocolManager.TYPE, type);
			
			result = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/***
	 * 获取如意竞猜详情列表
	 * @param userno
	 * @param pageIndex
	 * @param maxResult
	 * @return
	 */
	public String getRuyiGuessDetailList(String pageIndex, String maxResult, String userno,
			String id, String type) {
		String result = "";
		try {
			JSONObject jsonProtocol = ProtocolManager.getInstance()
					.getDefaultJsonProtocol();
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, DETAIL_REQUESTTYPE);
			jsonProtocol.put(ProtocolManager.PAGEINDEX, pageIndex);
			jsonProtocol.put(ProtocolManager.MAXRESULT, maxResult);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put("id", id);
			jsonProtocol.put(ProtocolManager.TYPE, type);
			result = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/***
	 * 获取如意竞猜详情列表
	 * @param userno
	 * @param pageIndex
	 * @param maxResult
	 * @return
	 */
	public String sendDateToService(String userno,
			String id, String info) {
		String result = "";
		try {
			JSONObject jsonProtocol = ProtocolManager.getInstance()
					.getDefaultJsonProtocol();
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.REQUESTTYPE, SEND_REQUESTTYPE);
			jsonProtocol.put(ProtocolManager.USERNO, userno);
			jsonProtocol.put("id", id);
			jsonProtocol.put("info", info);
			result = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.LOT_SERVER, jsonProtocol.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
