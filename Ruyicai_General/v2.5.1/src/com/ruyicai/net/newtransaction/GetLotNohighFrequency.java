package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.newtransaction.pojo.BetAndWinAndTrackAndGiftQueryPojo;
import com.ruyicai.util.Constants;
import com.ruyicai.util.ProtocolManager;

public class GetLotNohighFrequency {
	
	 private static String COMMAND = "QueryLot";	
	 private static GetLotNohighFrequency instance = null;
     private GetLotNohighFrequency(){
    	 
     }
     public  synchronized static GetLotNohighFrequency getInstance(){
    	 if(instance==null){
    		 instance = new GetLotNohighFrequency();
    	 }
    	 return instance;
     }
     
     public static String getInfo(String  lotno){
 		
 		JSONObject jsonProtocol = ProtocolManager.getInstance().getDefaultJsonProtocol();
 		try {
 			jsonProtocol.put(ProtocolManager.LOTNO,lotno);
 			jsonProtocol.put(ProtocolManager.COMMAND,COMMAND);
 			jsonProtocol.put(ProtocolManager.TYPE,ProtocolManager.HIGHFREQENCYTPYE);
 						
 			return InternetUtils.GetMethodOpenHttpConnectSecurity(Constants.LOT_SERVER, jsonProtocol.toString());
 			
 		} catch (JSONException e) {
 			e.printStackTrace();
 		}
 		
 		return "";
 	}
}
