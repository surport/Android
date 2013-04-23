package com.ruyicai.net.transaction;

import java.util.Random;

import org.json.JSONObject;

import com.ruyicai.net.InternetUtils;
import com.ruyicai.util.Constants;
import com.ruyicai.util.PublicMethod;
import com.ruyicai.util.URLEncoder;

public class ChangePasswordInterface {
	/**
	 * ¿Í»§¶ËÐÞ¸ÄÃÜÂë
	 */
	public static String changePassword(String mobileNumber, String oldPwd, String newPwd, String sessionid) {
		String action = "user.do";
		String method = "changePassword";
		String reValue = "";
		try {
			Random rdm = new Random();
			int transctionId = rdm.nextInt();
			String para = "";
			JSONObject paras = new JSONObject();
			paras.put("inputCharset", 2);
			paras.put("version", "v2.0");
			paras.put("language", 1);
			paras.put("transctionid", transctionId);
			paras.put("mobile_number", mobileNumber);
			paras.put("old_Pwd", oldPwd);
			paras.put("new_Pwd", newPwd);
			para = URLEncoder.encode(paras.toString());

			reValue = InternetUtils.GetMethodOpenHttpConnect(Constants.SERVER_URL + action
					+ ";jsessionid=" + sessionid + "?method=" + method
					+ "&jsonString=" + para);
			
		} catch (Exception e) {
			PublicMethod.myOutput(e.getMessage());
		}

		return reValue;
	}
}
