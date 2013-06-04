package com.ruyicai.net.transaction;

import org.json.JSONObject;

import android.util.Log;

import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.ProtocolManager;
import com.ruyicai.util.Constants;


/**
 * 反馈
 * @author haojie
 *
 */
public class FeedbackInterface {
	private static final String TAG = "FeedbackInterface";
	
	private static String COMMAND = "feedback";
	
	private static FeedbackInterface instance; 
	
	private FeedbackInterface() {
		
	}
	
	public synchronized static FeedbackInterface getInstance(){
		if(instance == null){
			instance = new FeedbackInterface();
		}
		return instance;
	}
	
	/**
	 * 提交服务器反馈内容
	 */
	public static String feedback(String feedbackContent) {
		String reValue = "";
		try {
			
			JSONObject jsonProtocol = ProtocolManager.getInstance().getDefaultJsonProtocol();
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(ProtocolManager.FEEDBACK_CONTENT, feedbackContent);
			reValue = InternetUtils.GetMethodOpenHttpConnectSecurity(Constants.LOT_SERVER,jsonProtocol.toString());
		} catch (Exception e) {
			Log.e(TAG, "softwareupeate error");
		}
		return reValue;
		

	}
}
