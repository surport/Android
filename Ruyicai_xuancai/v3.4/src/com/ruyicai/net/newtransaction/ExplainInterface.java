package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

public class ExplainInterface {
	private static  String COMMAND = "jingCai";
	

	private static ExplainInterface instance = null;
	private ExplainInterface(){
	}
	public synchronized static ExplainInterface getInstance(){
		if(instance == null){
			instance = new ExplainInterface();
		}		
		return instance;
	}
	/**
	 * 充值的方法
	 */
	public  static String getExplain(String event){
		String re = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance().getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND,COMMAND);
			jsonProtocol.put(ProtocolManager.REGUESTTYPE, "dataAnalysis");
			jsonProtocol.put(ProtocolManager.EVENT, event);	
			re = InternetUtils.GetMethodOpenHttpConnectSecurity(Constants.LOT_SERVER, jsonProtocol.toString());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return re;
	}
}
