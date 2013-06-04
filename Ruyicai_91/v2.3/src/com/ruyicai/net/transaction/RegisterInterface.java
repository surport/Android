package com.ruyicai.net.transaction;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.ruyicai.activity.home.HomePage;
import com.ruyicai.net.InternetUtils;
import com.ruyicai.net.ProtocolManager;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.URLEncoder;

/**
 * 用户注册接口调用
 * 
 * @author haojie
 * 
 */
public class RegisterInterface {
	
	private static RegisterInterface instance = null;
	private static String COMMAND = "register";
	private static String PHONENUM = "phonenum";
	private static String PASSWORD = "password";
	private static String CERTID = "certid";
	private static String REALNAME = "name";
	
	
	private RegisterInterface() {
		
	}
	
	public static synchronized RegisterInterface getInstance(){
		if(instance == null){
			instance = new RegisterInterface();
		}
		return instance;
	}
	
	String error_code = "00";

	public String userregister(String login_User, String login_password, String login_Id,String realname) {

		JSONObject obj = null;
		try {
			//String re = register(login_User, login_password, login_Id);
			
			String re = registerLotserver(login_User, login_password, login_Id, realname);
			obj = new JSONObject(re);
			error_code = obj.getString("errorCode");
		} catch (JSONException e) {

		}
		return error_code;

	}
	
	
	/**
	 * 调用 lotserver
	 */
	public String registerLotserver(String mobileCode,String password,String certid,String realname){
		
		JSONObject jsonProtocol = ProtocolManager.getInstance().getDefaultJsonProtocol();
		try {
			jsonProtocol.put(ProtocolManager.COMMAND, COMMAND);
			jsonProtocol.put(PHONENUM, mobileCode);
			jsonProtocol.put(PASSWORD, password);
			jsonProtocol.put(CERTID	, certid);
			jsonProtocol.put(REALNAME, realname);
			jsonProtocol.put("subchannel","00092494");
			
			
			return InternetUtils.GetMethodOpenHttpConnectSecurity(Constants.LOT_SERVER, jsonProtocol.toString());
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	
	/**
	 * 用户注册
	 * @param user_name    用户名（手机号码）
	 * @param password     密码
	 * @param id           身份证号默认18个1
	 * @return
	 */
	public String register(String mobile_code, String user_password,
			String id) {
		String action = "user.do";
		String method = "register";
		String channel_id = HomePage.channel_id;
		String channel = HomePage.channel;
		String reValue = "";
		try {
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("channel_id", channel_id);
			paras.put("channel", channel);
			paras.put("transctionid", transctionId);
			paras.put("mobile_code", mobile_code);
			paras.put("user_password", user_password);
			paras.put("Id", id);// 身份证号，默认18个1
			para = URLEncoder.encode(paras.toString());

			reValue = InternetUtils.GetMethodOpenHttpConnect(Constants.SERVER_URL + action+ "?method=" + method + "&jsonString=" + para);
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}
		return reValue;
	}
	
}
