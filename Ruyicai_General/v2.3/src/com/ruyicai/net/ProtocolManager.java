package com.ruyicai.net;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.util.Constants;

/**
 *@author haojie
 * 协议管理类
 * 作用是处理通讯协议发送，接收的组装
 * 通讯协议使用json格式
 * 数据在联网层做了加密
 * singleton 模式
 */
public class ProtocolManager {
	
	private final static String TAG = "ProtocolManager";
	
	public static String COMMAND = "command";
	public static String IMEI = "imei";
	public static String IMSI = "imsi";
	public static String SOFTWARE_VERSION = "softwareversion";
	public static String SMS_CENTER = "smscenter";
	public static String COOP_ID = "coopid";
	public static String MACHINE_ID = "machineid";
	public static String PHONE_NUM = "phonenum";
	public static String PASSWORD = "password";
	public static String PLATFORM = "platform";
	
	public static String START_DATE = "startdate";
	public static String END_DATE = "enddate";
	public static String ACCOUNT_DETAIL_TYPE = "type";
	public static String STOP_LINE = "stopLine";
	public static String START_LINE = "startLine";
	
	
	
	public static String GAME_STATINFO = "statinfo";
	
	public static String DOWNLOAD_URL = "downloadurl";
	
	public static String FEEDBACK_CONTENT = "content";
	
	private static JSONObject defaultJsonObject  = null;
	
	private static ProtocolManager protocolManager = null;
	
	private ProtocolManager() {
		
	}
	
	public synchronized static ProtocolManager getInstance(){
		if(protocolManager == null){
			protocolManager = new ProtocolManager();
			defaultJsonObject = new JSONObject();
		}
		return protocolManager;
	}
	
    public static JSONObject getDefaultJsonProtocol(){
    	try {
    		if(Constants.IMEI == null){
    			defaultJsonObject.put(IMEI, "");
			}else{
				defaultJsonObject.put(IMEI, Constants.IMEI);
			}
			if(Constants.IMSI == null){
				defaultJsonObject.put(IMSI, "");
			}else{
			    defaultJsonObject.put(IMSI, Constants.IMSI);
			}
			defaultJsonObject.put(SOFTWARE_VERSION, Constants.SOFTWARE_VERSION);
			defaultJsonObject.put(MACHINE_ID, Constants.MACHINE_ID);
			defaultJsonObject.put(COOP_ID, Constants.COOP_ID);
			defaultJsonObject.put(SMS_CENTER, Constants.SMS_CENTER);
			defaultJsonObject.put(PLATFORM, Constants.PLATFORM_ID);
			
		} catch (JSONException e) {
			Log.e(TAG, "");
		}
    	return defaultJsonObject;
    }
}
