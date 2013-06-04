package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.Constants;
import com.ruyicai.util.ProtocolManager;
/**
 * 
 * @author Administrator
 * 彩票资讯接口
 */
public class InformationInterface {
	
	private static String COMMAND = "information";
    public static InformationInterface instance=null;
    
    private InformationInterface(){
    	
    }
    
    public static InformationInterface getInstance(){
    	if(instance==null){
    		instance = new InformationInterface();
    	}
    	return instance;
    }
    
   /*获取彩票资讯
    * */
    public static String  getInformation(String type){
  
		JSONObject jsonProtocol = ProtocolManager.getInstance().getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND,COMMAND);
			jsonProtocol.put(ProtocolManager.INFORMATION_TYPE, type);
			
			Log.e("jsonProtocol-======", jsonProtocol.toString());
			return InternetUtils.GetMethodOpenHttpConnectSecurity(Constants.LOT_SERVER, jsonProtocol.toString());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return "";
	}
    	

}
