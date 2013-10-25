package com.ruyicai.net.newtransaction;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.constant.Constants;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.ProtocolManager;

/**
 * 第三方登录接口
 * 
 * @author Administrator
 * 
 */
public class ThirdPartyLoginInterface {

	private static ThirdPartyLoginInterface instance = null;

	private ThirdPartyLoginInterface() {
	}

	public synchronized static ThirdPartyLoginInterface getInstance() {
		if (instance == null) {
			instance = new ThirdPartyLoginInterface();
		}
		return instance;
	}
	
	public JSONObject thirdPartyLogin(String token,String plat){
		//TODO 第三方登录的网络加密和压缩协议有待商议
//		String result = "{\"value\": {\"accessurl\": \"http:www.ruyicai.com\",\"userno\": \"00001216\",\"certid\": \"\",\"mobileid\": \"18610000000\",\"name\": \"\",\"userName\": \"9043\",\"sessionid\": \"4CC12FE66F959E9757AA6F851516863B\"},\"errorcode\":\"0\",\"message\": \"登陆成功\"}";
		String result = "";
		JSONObject jsonProtocol = ProtocolManager.getInstance()
				.getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.TOKEN, token);
			jsonProtocol.put(ProtocolManager.PLAT, plat);
			result = InternetUtils.GetMethodOpenHttpConnectSecurity(
					Constants.THIRDPARTY_SERVER, jsonProtocol.toString());
			return new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		try {
			return new JSONObject(result);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
