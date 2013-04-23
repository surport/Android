package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.newtransaction.NoticeInterface;
import com.ruyicai.util.Constants;
import com.ruyicai.util.ProtocolManager;

public class NoticeJcInfo {
	private static NoticeJcInfo noticeInterface = null;
	
	private static String COMMAND = "QueryLot";
	
	private NoticeJcInfo (){
		
	}
	
	public synchronized static NoticeJcInfo getInstance(){
		if(noticeInterface == null){
			noticeInterface = new NoticeJcInfo();
		}
		return noticeInterface;
	}
	
	/**
	 * 获取所有的开奖信息
	 * @param jcType  "0"代表竞彩篮球 “1”代表竞彩足球
	 * @return
	 */
	public String getLotteryAllNotice(String jcType,String date){
		String result = "";
		try {
			JSONObject jsonProtocol = ProtocolManager.getInstance().getDefaultJsonProtocol();
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.TYPE,"jcResult");
			jsonProtocol.put(ProtocolManager.JCTYPE,jcType);
			jsonProtocol.put(ProtocolManager.DATE,date);
			result = InternetUtils.GetMethodOpenHttpConnectSecurity(Constants.LOT_SERVER,jsonProtocol.toString());
		} catch (JSONException e) {
			e.printStackTrace();
			//联网失败了
		}
		
		return result;
	}
}
